package dev.mfataka.esnzlin.jpa.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 19:40
 */
public enum UserRole {
    ADMIN,
    ESNER,
    ERASMUSER,
    BOARDMEMBER,
    UNKNOWN;

    private final static List<UserRole> USER_ROLES = Arrays.asList(UserRole.values());

    public static UserRole fromString(final String text) {
        return USER_ROLES.stream()
                .filter(userRole -> userRole.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public boolean isUnknown() {
        return this.equals(UNKNOWN);
    }
}
