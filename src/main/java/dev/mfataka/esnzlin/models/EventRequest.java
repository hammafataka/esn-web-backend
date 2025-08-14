package dev.mfataka.esnzlin.models;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.*;

import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Mono;

import dev.mfataka.esnzlin.jpa.domain.Event;
import dev.mfataka.esnzlin.jpa.domain.Image;
import dev.mfataka.esnzlin.jpa.enums.EventType;
import dev.mfataka.esnzlin.service.ImageService;


@Validated
public record EventRequest(
        Long id, // No validation needed
        @NotBlank(message = "Event title is required")
        @Size(max = 100, message = "Event name must not exceed 100 characters")
        String title,

        @NotBlank(message = "Event scope is required")
        @Pattern(regexp = "(?i)PUBLIC|PRIVATE", message = "Event scope must either be Public or Private")
        String eventType,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        String image, // No validation needed

        @NotNull(message = "Event start date and time is required")
        @FutureOrPresent(message = "Event start date and time must be in the future or present")
        LocalDateTime startDateTime,

        @NotNull(message = "Event end date and time is required")
        @Future(message = "Event end date and time must be in the future")
        LocalDateTime endDateTime,
        @NotBlank(message = "Event address is required")
        @Size(max = 200, message = "Event address must not exceed 200 characters")
        String eventAddress,

        @NotNull(message = "Price is required")
        Double price
) {

    @Override
    public String toString() {
        final var image = Objects.isNull(this.image) ? null : "<Image>";
        return "EventRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", eventType='" + eventType + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", eventAddress='" + eventAddress + '\'' +
                ", amount=" + price +
                '}';
    }

    public Mono<Event> asEvent(final ImageService imageService) {
        final var encodedImage = Image.fromBase64(image);
        return imageService.saveImage(encodedImage)
                .map(image -> Event.builder()
                        .id(id == null ? 0 : id)
                        .title(title)
                        .eventType(EventType.fromString(eventType))
                        .description(description)
                        .imageId(image.getId())
                        .startDttm(startDateTime)
                        .endDttm(endDateTime)
                        .price(price)
                        .createdTime(LocalDateTime.now())
                        .eventAddress(eventAddress)
                        .currency("CZK")
                        .build());

    }

}
