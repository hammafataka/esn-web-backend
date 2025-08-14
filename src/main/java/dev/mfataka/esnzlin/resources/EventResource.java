package dev.mfataka.esnzlin.resources;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.mfataka.esnzlin.jpa.domain.Event;
import dev.mfataka.esnzlin.models.EventRequest;
import dev.mfataka.esnzlin.models.EventResponse;
import dev.mfataka.esnzlin.service.EventService;
import dev.mfataka.esnzlin.utils.BaseResponse;

@Slf4j
@RestController
@RequestMapping(path = "${endpoint.path.external}/event")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Event Resource")
public class EventResource {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<EventResponse> saveEvent(@RequestBody @Valid EventRequest eventRequest) {
        return eventService.saveEvent(eventRequest);
    }


    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<BaseResponse<EventResponse>> updateEvent(@RequestBody EventRequest eventRequest) {
        return eventService.saveEvent(eventRequest)
                .map(BaseResponse::ok);
    }

    @GetMapping("/list")
    public Mono<List<EventResponse>> getAllEvents() {
        return eventService.listAllEvents(Pageable.unpaged())
                .map(Slice::getContent);
    }

    @GetMapping("/list/{startDate}")
    public Flux<Event> getEventsBetweenDates(@PathVariable String startDate) {
        return eventService.getEventsBetweenDates(startDate);
    }

    @GetMapping("/list/{id}")
    public Mono<BaseResponse<Event>> getEventById(@PathVariable("id") Long id) {
        return eventService.findEventById(id)
                .map(BaseResponse::ok);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<BaseResponse<String>> deleteEvent(@PathVariable("id") Long id) {
        return eventService.deleteEvent(id)
                .map(BaseResponse::ok);
    }

}