package dev.mfataka.esnzlin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.jpa.domain.Event;
import dev.mfataka.esnzlin.jpa.repository.EventAttendeeRepository;
import dev.mfataka.esnzlin.jpa.repository.EventRepository;
import dev.mfataka.esnzlin.models.EventRequest;
import dev.mfataka.esnzlin.models.EventResponse;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventAttendeeRepository eventAttendeeRepository;

    public Mono<EventResponse> saveEvent(final EventRequest request) {
        return request.asEvent(imageService)
                .flatMap(event -> {
                    event.setCreatedTime(LocalDateTime.now());
                    return eventRepository.save(event)
                            .flatMap(savedEvent -> savedEvent.toEventResponse(imageService, findAttendeesSize(savedEvent.getId())));
                })
                .doOnError(error -> log.error("An error occurred while saving the event: {}", error.getMessage(), error));


    }


    public Mono<Page<EventResponse>> listAllEvents(final Pageable pageable) {
        log.info("Listing all events");
        return eventRepository.findAllBy(pageable)
                .flatMap(event -> event.toEventResponse(imageService, findAttendeesSize(event.getId())))
                .collectList()
                .doOnNext(events -> log.info("Found {} events", events.size()))
                .zipWith(eventRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));

    }

    /**
     * I want a Year month day format for example 130824
     * I could use Regex to validate the dates but your ass can do that on front end
     */
    public Flux<Event> getEventsBetweenDates(final String startDate) {
        final var startDateTime = getStartOfDay(startDate);
        log.info("Listing all events from {} }", startDateTime);
        return eventRepository.findEventsBetweenDates(startDateTime);
    }


    public Mono<Event> findEventById(final Long id) {
        log.info("Finding event by id [{}]", id);
        return eventRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Event not found for id: " + id)))
                .doOnError(e -> log.error("Error finding event by id [{}]: {}", id, e.getMessage()));
    }

    // TODO maybe pull the object, and if empty is returned then run .switchIfEmpty
    public Mono<String> deleteEvent(long eventId) {
        log.info("Deleting event [{}]", eventId);
        return eventRepository.deleteById(eventId)
                .then(Mono.just("Event deleted successfully"))
                .onErrorResume(e -> {
                    log.error("Error deleting event with id [{}]: {}", eventId, e.getMessage());
                    return Mono.just("Error deleting event: " + e.getMessage());
                });
    }

    public static LocalDateTime getStartOfDay(String dateString) {
        final var formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        final var date = LocalDate.parse(dateString, formatter);
        return LocalDateTime.of(date, LocalDateTime.MIN.toLocalTime());
    }

    private Mono<Long> findAttendeesSize(final long eventId) {
        return eventAttendeeRepository.countByEventId(eventId);
    }

    public static LocalDateTime getEndOfDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return LocalDateTime.of(date, LocalDateTime.MAX.toLocalTime());
    }
}
