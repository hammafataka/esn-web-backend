package dev.mfataka.esnzlin.jpa.enums;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 07.10.2024 3:46
 */
public enum UserProperties {
    CURRENCY;

    public boolean isCurrency() {
        return this == CURRENCY;
    }
}
