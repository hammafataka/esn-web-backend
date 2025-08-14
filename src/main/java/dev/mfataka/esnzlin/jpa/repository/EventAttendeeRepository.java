package dev.mfataka.esnzlin.jpa.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import dev.mfataka.esnzlin.jpa.domain.EventAttendee;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 10.10.2024 15:33
 */
@Repository
public interface EventAttendeeRepository extends R2dbcRepository<EventAttendee, Long> {


    Mono<Long> countByEventId(long eventId);
}
