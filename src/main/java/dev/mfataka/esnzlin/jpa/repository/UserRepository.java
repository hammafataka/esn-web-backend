package dev.mfataka.esnzlin.jpa.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;

import dev.mfataka.esnzlin.jpa.domain.User;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:54
 */
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<Boolean> existsByEmail(final String email);

    Mono<User> findByEmail(final String email);
}
