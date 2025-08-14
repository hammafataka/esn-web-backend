package dev.mfataka.esnzlin.models;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 27.01.2025 13:25
 */

public record PaymentIntentResponse(String clientSecret, String paymentId) {
}
