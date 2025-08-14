package dev.mfataka.esnzlin.config.security;

import static dev.mfataka.esnzlin.constants.JwtConstants.JWT_HEADER;
import static dev.mfataka.esnzlin.constants.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.security.SignatureException;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 0:08
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtTokenFilter implements WebFilter {

    @Value("${allowed.paths}")
    private List<String> allowedPaths;

    private final ReactiveUserDetailsService userDetailsService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        allowedPaths = allowedPaths.stream()
                .map(s -> s.replace("**", ""))
                .toList();
    }

    /**
     * Main filter logic, adapted for WebFlux and reactive streams.
     */
    @NotNull
    @Override
    public Mono<Void> filter(final @NotNull ServerWebExchange exchange, @NotNull final WebFilterChain chain) {
        final var request = exchange.getRequest();

        // Skip filter for allowed paths
        if (shouldNotFilter(request)) {
            return chain.filter(exchange);
        }

        final var jwt = request.getHeaders().getFirst(JWT_HEADER);

        // If JWT is missing or does not start with Bearer, reject the request
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            log.warn("Unauthorized request with no Bearer token.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract token and check if expired
        final var token = jwt.replace("Bearer ", "");
        if (JwtService.isJwtTokenExpired(token)) {
            log.warn("Unauthorized request with expired token.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Check if authentication is already set
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    if (securityContext.getAuthentication() != null) {
                        log.info("Already authenticated, continue the chain");
                        return chain.filter(exchange);  // Already authenticated, continue the chain
                    }
                    log.info("Processing token authentication");
                    return processTokenAuthentication(token, exchange, chain);
                })
                .switchIfEmpty(processTokenAuthentication(token, exchange, chain));
    }

    private Mono<Void> processTokenAuthentication(final String token, final ServerWebExchange exchange, final WebFilterChain chain) {
        // Extract email from token
        return Mono.fromSupplier(() -> JwtService.extractClaim(token, claims -> claims.get("email", String.class)))
                .flatMap(email -> {
                    if (email == null) {
                        log.warn("Unauthorized request - email not found in token.");
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                    // Load user details in a reactive manner and validate token
                    return userDetailsService.findByUsername(email)
                            .flatMap(userDetails -> {
                                if (!JwtService.isTokenValid(token, userDetails)) {
                                    log.warn("Unauthorized request with invalid token.");
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    return exchange.getResponse().setComplete();
                                }

                                // Create authentication object and set it in the security context
                                final var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                return chain.filter(exchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                log.warn("Unauthorized request - user not found.");
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }));
                })
                .onErrorResume(SignatureException.class, e -> {
                    final var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, null)
                            .httpOnly(true)
                            //todo production  .secure(true)
                            .path("/")
                            .maxAge(0)
                            .build();
                    exchange.getResponse().getHeaders().add("Set-Cookie", cookie.toString());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    /**
     * This method checks if the request path is part of allowed paths.
     */
    private boolean shouldNotFilter(final ServerHttpRequest request) {
        return allowedPaths.stream().anyMatch(path -> request.getURI().getPath().startsWith(path));
    }
}