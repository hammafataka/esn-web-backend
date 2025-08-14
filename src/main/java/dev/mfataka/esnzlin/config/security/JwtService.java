package dev.mfataka.esnzlin.config.security;

import static dev.mfataka.esnzlin.constants.JwtConstants.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.utils.TimeUtils;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 0:08
 */
@Slf4j
public class JwtService {

    public static String regenerateAccessToken(final String token) {
        final var email = getEmailFromJwtToken(token);
        return generateAccessToken(email);
    }

    public static String generateAccessToken(final String email) {
        return generateToken(email, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public static String generateVerificationToken(final String email) {
        return generateToken(email, VERIFICATION_TOKEN_EXPIRATION_TIME);
    }

    public static String generateRefreshToken(final String email) {
        return generateToken(email, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public static boolean isTokenValid(final String token, final UserDetails userDetails) {
        final var email = getEmailFromJwtToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public static String getEmailFromJwtToken(final String jwt) {
        return extractClaim(jwt, claims -> claims.get("email", String.class));
    }

    public static boolean isJwtTokenExpired(final String token) {
        final var plainToken = token.replace("Bearer ", ""); // Assuming "Bearer " is removed from the token
        final var jwt = JWT.decode(plainToken);
        return !jwt.getExpiresAt().after(new Date());
    }

    public static boolean isJwtTokenNotExpired(final String token) {
        return !isJwtTokenExpired(token);
    }


    public static Mono<String> extractEmail(final String token) {
        return Mono.justOrEmpty((String) extractClaim(token, claims -> claims.get("email", String.class)));
    }

    public static <T> T extractClaim(final String token, final Function<Claims, T> function) {
        return function.apply(extractClaims(token));
    }

    public static Claims extractClaims(final String token) {
        final var tokenPlain = token.replace("Bearer ", ""); // Assuming "Bearer " is removed from the token
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(tokenPlain)
                .getPayload();
    }


    private static String generateToken(final String email, final Duration expiration) {
        final var expirationTime = LocalDateTime.now().plus(expiration);
        return Jwts.builder()
                .issuedAt(new Date())
                .subject(email)
                .expiration(TimeUtils.convertLocalDateTimeToDate(expirationTime))
                .claim("email", email)
                .signWith(SECRET_KEY)
                .compact();
    }

    private static boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
