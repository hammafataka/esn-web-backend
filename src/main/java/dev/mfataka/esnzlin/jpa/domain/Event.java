package dev.mfataka.esnzlin.jpa.domain;

import static dev.mfataka.esnzlin.constants.HtmlConstants.getPaymentItemHtml;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import reactor.core.publisher.Mono;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.EventType;
import dev.mfataka.esnzlin.models.EventResponse;
import dev.mfataka.esnzlin.service.ImageService;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    private long id;

    @Column(value = "title")
    private String title;

    @Column(value = "event_type")
    private EventType eventType;

    @Column(value = "description")
    private String description;

    @Column(value = "start_dttm")
    private LocalDateTime startDttm;

    @Column(value = "end_dttm")
    private LocalDateTime endDttm;

    @Column(value = "created_time")
    private LocalDateTime createdTime;

    @Column(value = "address")
    private String eventAddress;


    @Column(value = "image_id")
    private long imageId;

    private Double price;

    private String currency;

    public Mono<EventResponse> toEventResponse(final ImageService imageService, final Mono<Long> attendeesSize) {
        final var builder = EventResponse.builder()
                .id(this.id)
                .title(this.title)
                .eventType(this.eventType)
                .description(this.description)
                .startDateTime(this.startDttm)
                .endDateTime(this.endDttm)
                .createdTime(this.createdTime)
                .eventAddress(this.eventAddress)
                .price(this.price)
                .currency(this.currency);
        return attendeesSize.flatMap(attendees -> {
            builder.attendees(attendees);
            return imageService.getImageById(imageId)
                    .map(image -> builder.image(image.asBase64(imageService))
                            .build())
                    .defaultIfEmpty(builder.build());
        });

    }

    public Mono<String> asPaymentItemHtml(final ImageService imageService) {
        return imageService.getImageById(imageId)
                .map(image -> image.asBase64(imageService))
                .map(image -> getPaymentItemHtml(image, title, description, price.longValue()));
    }
}
