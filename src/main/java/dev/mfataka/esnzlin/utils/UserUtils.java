package dev.mfataka.esnzlin.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import reactor.core.publisher.Mono;

import lombok.experimental.UtilityClass;

import dev.mfataka.esnzlin.jpa.domain.User;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 27.01.2025 14:32
 */
@UtilityClass
public class UserUtils {

    public static Mono<User> retrieveCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User) securityContext.getAuthentication().getPrincipal());
    }
}
