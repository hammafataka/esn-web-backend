package dev.mfataka.esnzlin.jpa.enums;

import java.util.Arrays;
import java.util.List;

public enum EventVisibility {

    VISIBLE,

    INVISIBLE;

    public final static List<EventVisibility> EVENT_VISIBILITY = Arrays.asList(EventVisibility.values());

    public static EventVisibility fromString(String eventVisibility) {
        return EVENT_VISIBILITY
                .stream()
                .filter(event -> event.name().equalsIgnoreCase(eventVisibility))
                .findFirst()
                .orElse(VISIBLE);
    }

}
