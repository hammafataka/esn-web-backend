package dev.mfataka.esnzlin.jpa.enums;

import java.util.Arrays;
import java.util.List;

public enum EventType {

    PUBLIC,

    PRIVATE;

    public final static List<EventType> EVENT_TYPES = Arrays.asList(EventType.values());

    public static EventType fromString(String eventType) {
        return EVENT_TYPES
                .stream()
                .filter(event -> event.name().equalsIgnoreCase(eventType))
                .findFirst()
                .orElse(PUBLIC);
    }


}
