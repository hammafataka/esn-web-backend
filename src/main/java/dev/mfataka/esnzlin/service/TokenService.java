package dev.mfataka.esnzlin.service;

import org.springframework.security.authentication.BadCredentialsException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.config.security.JwtService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 23.08.2024 16:30
 */
@Slf4j
@UtilityClass
public class TokenService {

    public String generateAccessToken(final String refreshToken) {
        log.info("Generating access token from refresh token");
        if (refreshToken == null) {
            throw new BadCredentialsException("Refresh token is null");
        }
        final var jwtTokenExpired = JwtService.isJwtTokenExpired(refreshToken);
        if (jwtTokenExpired) {
            throw new BadCredentialsException("Refresh token is expired");
        }
        log.info("Access token generated successfully");
        return JwtService.regenerateAccessToken(refreshToken);
    }

}
