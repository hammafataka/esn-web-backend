package dev.mfataka.esnzlin.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.mfataka.esnzlin.jpa.enums.EventType;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 28.08.2024 23:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private long id;
    private String title;
    private EventType eventType;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createdTime;
    private String eventAddress;
    private long attendees; // Assuming this represents the number of attendees
    private String image; // Assuming this is the URL or path to the image
    private double price;
    private String currency;
}
