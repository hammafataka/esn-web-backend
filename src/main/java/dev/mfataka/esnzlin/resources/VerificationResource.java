package dev.mfataka.esnzlin.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.constants.HtmlConstants;
import dev.mfataka.esnzlin.service.VerificationService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:55
 */
@Slf4j
@RestController
@RequestMapping(path = "${endpoint.path.verification}")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Verification Resource")
public class VerificationResource {

    private final VerificationService verificationService;


    @GetMapping(path = "${endpoint.path.verify}/{token}/token")
    public Mono<String> verifyEmail(@PathVariable("token") final String token) {
        return verificationService.verifyEmail(token)
                .map(result -> HtmlConstants.getSuccessVerificationHtml(5000))
                .onErrorResume(error -> Mono.just(HtmlConstants.getFailedVerificationHtml(error.getMessage())));
    }

}
