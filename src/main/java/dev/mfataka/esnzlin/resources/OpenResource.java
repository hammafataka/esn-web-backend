package dev.mfataka.esnzlin.resources;

import static dev.mfataka.esnzlin.jpa.enums.Interest.getAllInterests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.Faculty;
import dev.mfataka.esnzlin.jpa.enums.Interest;
import dev.mfataka.esnzlin.models.EventResponse;
import dev.mfataka.esnzlin.service.EventService;
import dev.mfataka.esnzlin.utils.BaseResponse;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 17:21
 */
@RestController
@RequestMapping(path = "${endpoint.path.open}")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Open Resource")
public class OpenResource {
    private final EventService eventService;

    @GetMapping("/event/list")
    public Mono<Page<EventResponse>> getEventsBetweenDates(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return eventService.listAllEvents(PageRequest.of(page, size));
    }

    @GetMapping("/available/interests")
    public List<Interest.InterestDTO> getInterests() {
        return getAllInterests();
    }

    @GetMapping("/available/faculties")
    public BaseResponse<List<Faculty.FacultyDto>> getFaculties() {
        return BaseResponse.ok(Faculty.getFacultiesForWeb());
    }
}
