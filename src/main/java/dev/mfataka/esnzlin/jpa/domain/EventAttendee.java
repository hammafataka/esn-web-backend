package dev.mfataka.esnzlin.jpa.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 28.08.2024 22:35
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_attendees")
public class EventAttendee {
    @Id
    private long id;

    @Column(value = "event_id")
    private long eventId;

    @Column(value = "user_id")
    private long userId;

    @Column(value = "payment_id")
    private long paymentId;

    @Column(value = "is_attending")
    private boolean isAttending;

    @Column(value = "is_interested")
    private boolean isInterested;

    @Column(value = "is_paid")
    private boolean isPaid;
}
