package dev.mfataka.esnzlin.models;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 3:08
 */
public record PaymentIntentRequest(Double amount, String currency) {
    @Override
    public String currency() {
        return switch (currency) {
            case "Kč" -> "CZK";
            case "€" -> "EUR";
            case "$" -> "USD";
            default -> currency;
        };
    }
}
