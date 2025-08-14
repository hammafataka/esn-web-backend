package dev.mfataka.esnzlin.jpa.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Flux;

import dev.mfataka.esnzlin.jpa.domain.Image;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 13:34
 */
public interface ImageRepository extends R2dbcRepository<Image, Long> {
    Optional<Image> findByName(final String name);

    Optional<Image> findByType(final String type);


    @Query("SELECT i FROM Image i WHERE i.lastUsedAt >:lastUsedAt ")
    Flux<Image> findAllByLastUsedAtAfter(final LocalDateTime lastUsedAt);
}
