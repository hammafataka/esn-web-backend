package dev.mfataka.esnzlin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.jpa.domain.EventAttendee;
import dev.mfataka.esnzlin.jpa.repository.EventAttendeeRepository;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 27.01.2025 14:09
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventAttendeeService {

    private final EventAttendeeRepository eventAttendeeRepository;


    public Flux<EventAttendee> saveAllEventAttendees(final Flux<EventAttendee> attendees) {
        return eventAttendeeRepository.saveAll(attendees);
    }
}
