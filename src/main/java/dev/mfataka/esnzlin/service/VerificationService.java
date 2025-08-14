package dev.mfataka.esnzlin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.config.security.JwtService;
import dev.mfataka.esnzlin.exceptions.UserDisplayableException;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:50
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VerificationService {
    private final UserService userService;

    public Mono<String>verifyEmail(final String token) {
        final var emailFromJwtToken = JwtService.getEmailFromJwtToken(token);
        return userService.findByEmailOrThrow(emailFromJwtToken)
                .flatMap(dbUser -> {
                    final var jwtTokenValid = JwtService.isJwtTokenNotExpired(token);
                    if (!jwtTokenValid) {
                        return Mono.error(new UserDisplayableException("Invalid or expired token"));
                    }

                    final var emailMatches = emailFromJwtToken.equals(dbUser.getEmail());
                    if (!emailMatches) {
                        return Mono.error(new UserDisplayableException("Email does not match the token"));
                    }
                    dbUser.setVerified(true);
                    return userService.saveUser(dbUser)
                            .map(user -> "Email verified successfully");
                })
                .onErrorMap(UserDisplayableException::new);
    }
}
