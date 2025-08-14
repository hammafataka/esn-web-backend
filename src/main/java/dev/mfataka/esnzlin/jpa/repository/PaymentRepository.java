package dev.mfataka.esnzlin.jpa.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import dev.mfataka.esnzlin.jpa.domain.Payment;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 27.01.2025 14:18
 */
public interface PaymentRepository extends R2dbcRepository<Payment, Long> {
}
