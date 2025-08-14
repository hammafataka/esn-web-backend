package dev.mfataka.esnzlin.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.models.PaymentIntentRequest;
import dev.mfataka.esnzlin.models.PaymentIntentResponse;
import dev.mfataka.esnzlin.service.PaymentService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 30.09.2024 23:51
 */
@RestController
@RequestMapping(path = "${endpoint.path.open}/payment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Open Payment Resource")
public class OpenPaymentResource {
    private final PaymentService paymentService;


    @PostMapping(value = "/payment-intent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaymentIntentResponse> getPaymentIntent(@RequestBody final Mono<PaymentIntentRequest> paymentIntentRequest) {
        return paymentService.makePaymentIntent(paymentIntentRequest);
    }
}
