package dev.mfataka.esnzlin.jpa.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.*;

import dev.mfataka.esnzlin.jpa.enums.PaymentStatus;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 28.08.2024 22:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    private long id;

    @Column(value = "payment_intent_id")
    private String paymentIntentId;

    @Column(value = "amount")
    private double amount;

    private PaymentStatus status;

}
