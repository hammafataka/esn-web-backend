package dev.mfataka.esnzlin.jpa.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Flux;

import dev.mfataka.esnzlin.jpa.domain.Event;

public interface EventRepository extends R2dbcRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.startDttm>:startDate")
    Flux<Event> findEventsBetweenDates(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT * FROM Event ")
    Flux<Event> findAllBy(Pageable pageable);
}
