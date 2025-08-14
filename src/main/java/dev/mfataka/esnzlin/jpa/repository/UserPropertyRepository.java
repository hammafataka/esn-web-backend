package dev.mfataka.esnzlin.jpa.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import dev.mfataka.esnzlin.jpa.domain.UserProperty;
import dev.mfataka.esnzlin.jpa.enums.UserProperties;
import dev.mfataka.esnzlin.jpa.keys.UserPropertyKey;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 3:49
 */
public interface UserPropertyRepository extends R2dbcRepository<UserProperty, UserPropertyKey> {
    @Query("SELECT up FROM UserProperty up WHERE up.id.userId = ?1")
    Flux<UserProperty> findAllByUserId(long userId);


    @Cacheable(value = "userProperties", key = "#userId")
    default Mono<UserProperty> findUserCurrency(long userId) {
        return findByUserIdAndKEy(userId, UserProperties.CURRENCY);
    }

    @Query("SELECT up FROM UserProperty up WHERE up.id.userId = ?1 AND up.id.key = ?2")
    Mono<UserProperty> findByUserIdAndKEy(long userId, UserProperties key);
}
