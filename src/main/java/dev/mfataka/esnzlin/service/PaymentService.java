package dev.mfataka.esnzlin.service;

import static dev.mfataka.esnzlin.constants.HtmlConstants.getPaymentConfirmationHtml;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.jpa.domain.Event;
import dev.mfataka.esnzlin.jpa.domain.EventAttendee;
import dev.mfataka.esnzlin.jpa.domain.Payment;
import dev.mfataka.esnzlin.jpa.domain.User;
import dev.mfataka.esnzlin.jpa.enums.PaymentStatus;
import dev.mfataka.esnzlin.jpa.repository.PaymentRepository;
import dev.mfataka.esnzlin.models.PaymentConfirmationRequest;
import dev.mfataka.esnzlin.models.PaymentIntentRequest;
import dev.mfataka.esnzlin.models.PaymentIntentResponse;
import dev.mfataka.esnzlin.utils.UserUtils;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 30.09.2024 23:47
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentService {

    @Value("${stripe.secret.key}")
    private final String API_SECRET_KEY;
    private final EventAttendeeService eventAttendeeService;
    private final PaymentRepository paymentRepository;
    private final ImageService imageService;
    private final MailService mailService;
    private final EventService eventService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Stripe.apiKey = API_SECRET_KEY;
    }

    public Mono<PaymentIntentResponse> makePaymentIntent(final Mono<PaymentIntentRequest> request) {
        return request.map(this::createPaymentIntent)
                .map(paymentIntent -> new PaymentIntentResponse(paymentIntent.getClientSecret(), paymentIntent.getId()));
    }

    @SneakyThrows
    public PaymentIntent createPaymentIntent(final PaymentIntentRequest request) {
        final var params = PaymentIntentCreateParams.builder()
                .setAmount(request.amount().longValue() * 100)
                .setCurrency(request.currency())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        return PaymentIntent.create(params);
    }

    public Mono<String> confirmPayment(final PaymentConfirmationRequest request) {
        return createPayment(request).flatMap(payment -> createEventAttendeeAndSendConfirmation(request, payment));
    }

    @NotNull
    private Mono<String> createEventAttendeeAndSendConfirmation(final PaymentConfirmationRequest request, Payment payment) {
        return UserUtils.retrieveCurrentUser()
                .flatMap(user -> createEventAttendees(request, payment, user.getId())
                        .map(events -> {
                            sendMail(payment, user, events);
                            log.info("Payment {} created successfully", payment.getId());
                            return "Payment confirmed successfully";
                        }));

    }

    @NotNull
    private Mono<List<Event>> createEventAttendees(final PaymentConfirmationRequest request, final Payment payment, final long userId) {
        return createEventAttendees(request, userId, payment.getId())
                .flatMap(eventAttendee -> eventService.findEventById(eventAttendee.getEventId()))
                .collectList();
    }

    private void sendMail(final Payment payment, final User user, final List<Event> events) {
        getPaymentConfirmationHtml(user.getFullName(), payment.getId(), imageService, events, (long) payment.getAmount())
                .subscribe(html -> mailService.sendMail(html, user.getEmail(), "Payment Confirmation"));
    }

    @SneakyThrows
    private static PaymentIntent retrievePaymentIntent(final PaymentConfirmationRequest request) {
        return PaymentIntent.retrieve(request.paymentIntentId());
    }


    private Flux<EventAttendee> createEventAttendees(final PaymentConfirmationRequest request, final long userId, final long paymentId) {
        final var publisher = Flux.fromIterable(request.items())
                .map(confirmationItem -> createEventAttendee(confirmationItem, userId, paymentId));
        return eventAttendeeService.saveAllEventAttendees(publisher);
    }

    private Mono<Payment> createPayment(final PaymentConfirmationRequest request) {
        final var paymentIntent = retrievePaymentIntent(request);
        final var amount = paymentIntent.getAmount();
        final var payment = Payment.builder()
                .paymentIntentId(request.paymentIntentId())
                .amount(amount)
                .status(PaymentStatus.valueOf(paymentIntent.getStatus().toUpperCase()))
                .build();
        return paymentRepository.save(payment);
    }

    private static EventAttendee createEventAttendee(final PaymentConfirmationRequest.ConfirmationItem request, final long userId, final long paymentId) {
        return EventAttendee.builder()
                .eventId(Long.parseLong(request.id()))
                .userId(userId)
                .isPaid(true)
                .isAttending(true)
                .paymentId(paymentId)
                .build();
    }
}
