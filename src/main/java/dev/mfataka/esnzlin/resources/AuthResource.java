package dev.mfataka.esnzlin.resources;

import static dev.mfataka.esnzlin.constants.JwtConstants.JWT_HEADER;
import static dev.mfataka.esnzlin.constants.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import java.time.Duration;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.config.security.JwtService;
import dev.mfataka.esnzlin.models.SignInRequest;
import dev.mfataka.esnzlin.models.UserDetailsResponse;
import dev.mfataka.esnzlin.models.UserRequest;
import dev.mfataka.esnzlin.service.TokenService;
import dev.mfataka.esnzlin.service.UserService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 17:43
 */

@Slf4j
@RestController
@RequestMapping(path = "${endpoint.path.external}/auth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Authorization Resource")
public class AuthResource {

    private final UserService userService;

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> signup(@RequestBody final UserRequest userRequest) {
        return userService.signUpUser(userRequest)
                .map(result -> {
                    final var headers = buildHeaders(userRequest.getEmail());
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(result);
                });
    }

    @PostMapping(path = "/signin")
    public Mono<ResponseEntity<UserDetailsResponse>> signin(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) final String refreshToken,
            @RequestBody @Valid final SignInRequest signInRequest) {

        // Handle the request reactively
        return userService.signIn(signInRequest, refreshToken)
                .map(result -> {
                    // Build headers and return the ResponseEntity
                    var headers = buildHeaders(result.getEmail());
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(result);
                });
    }


    /**
     * Refreshes the access token using the refresh token. access token is what frontend uses to access protected resources,
     * refresh token is used to get a new access token and stored in a httpOnly cookie for security reasons,
     * if the refresh token is expired the user will have to log in again
     *
     * @param refreshToken the refresh token
     * @return the new access token
     */

    @GetMapping(path = "/refresh-token", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> refreshToken(@CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        final var accessToken = TokenService.generateAccessToken(refreshToken);
        return ResponseEntity.ok("Bearer " + accessToken);
    }


    /**
     * Validates the credentials of the user, checks if the email is already taken and if password is valid based on validation of the dto
     *
     * @param signInRequest the sign in request
     * @return the response entity
     */
    @PostMapping(path = "/validate/credentials")
    public Mono<String> validateCredentials(@RequestBody @Valid final SignInRequest signInRequest) {
        return userService.findByEmail(signInRequest.getEmail())
                .map(user -> "email is already taken")
                .defaultIfEmpty("credentials are valid");
    }

    private static HttpHeaders buildHeaders(final String user) {
        final var accessToken = JwtService.generateAccessToken(user);
        final var refreshToken = JwtService.generateRefreshToken(user);
        final var refreshTokenCookie = buildResponseCookie(refreshToken);

        final var headers = new HttpHeaders();
        headers.add(JWT_HEADER, "Bearer " + accessToken);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return headers;
    }

    private static ResponseCookie buildResponseCookie(final String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
//todo production  .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
    }


}
