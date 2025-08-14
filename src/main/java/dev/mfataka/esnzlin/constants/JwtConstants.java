package dev.mfataka.esnzlin.constants;


import java.time.Duration;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

import lombok.experimental.UtilityClass;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 0:08
 */
@UtilityClass
public class JwtConstants {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS512.key().build();
    public static final String JWT_HEADER = "Authorization";
    public static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofHours(1);
    public static final Duration VERIFICATION_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(5);
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
}
