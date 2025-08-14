package dev.mfataka.esnzlin.models;

import java.util.List;

import dev.mfataka.esnzlin.models.enums.ProductType;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 27.01.2025 13:05
 */


public record PaymentConfirmationRequest(String paymentIntentId, List<ConfirmationItem> items) {
    public record ConfirmationItem(String id, String name, ProductType productType) {

    }
}
