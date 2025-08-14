package dev.mfataka.esnzlin.resources;

import static dev.mfataka.esnzlin.constants.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.models.UserRequest;
import dev.mfataka.esnzlin.service.UserService;
import dev.mfataka.esnzlin.utils.BaseResponse;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:23
 */
@Slf4j
@RestController
@RequestMapping(path = "${endpoint.path.external}/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "User Resource")
public class UserResource {

    private final UserService userService;

    @GetMapping(path = "/handshake")
    public Mono<String> handshake() {
        return Mono.just("Hello, I am alive");
    }

    @GetMapping(path = "/verify/{email}")
    public Mono<BaseResponse<String>> verifyEmail(@PathVariable("email") final String email) {
        return userService.sendVerificationEmail(email)
                .map(BaseResponse::ok);
    }


    @GetMapping(path = "/verify/result/{email}")
    public Mono<BaseResponse<String>> verifyEmailResult(@PathVariable("email") final String email) {
        return userService.verificationEmailResult(email)
                .map(BaseResponse::ok);
    }


    @GetMapping(path = "/logout")
    public ResponseEntity<String> logout() {
        log.info("Logging out user");
        final var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, null)
                .httpOnly(true)
//todo production  .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("ok");
    }


    @PostMapping(path = "/update/about/")
    public Mono<BaseResponse<String>> updateAbout(@RequestBody @Valid final UserRequest.AboutRequest request,
                                                  @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        return userService.updateAbout(request, refreshToken)
                .map(BaseResponse::ok);

    }
}
