package dev.mfataka.esnzlin.jpa.enums;

import lombok.Getter;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 28.08.2024 22:51
 */
@Getter
public enum PaymentStatus {
    PENDING,
    SUCCEEDED,
    CANCELLED,
    FAILED;
}
