package dev.mfataka.esnzlin.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.models.PaymentConfirmationRequest;
import dev.mfataka.esnzlin.service.PaymentService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 22.10.2024 16:51
 */
@Slf4j
@RestController
@Tag(name = "Payment Resource")
@RequestMapping(path = "${endpoint.path.external}/payment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentResource {
    private final PaymentService paymentService;

    @PostMapping("/confirmation")
    public Mono<String> paymentConfirmation(@RequestBody PaymentConfirmationRequest paymentConfirmationRequest) {
        return paymentService.confirmPayment(paymentConfirmationRequest);
    }
}
