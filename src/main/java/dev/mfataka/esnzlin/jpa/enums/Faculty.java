package dev.mfataka.esnzlin.jpa.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 05.08.2024 17:22
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum Faculty {
    FT("Faculty of Technology"),
    FMK("Faculty of Multimedia and communications"),
    FHS("Faculty of Humanitarian studies"),
    FAI("Faculty of Applied informatics"),
    FAME("Faculty of Management and Economics"),
    FLKR("Faculty of Logistics and Crisis Management"),
    UNKNOWN("Unknown");

    private final static List<Faculty> FACULTIES = Arrays.asList(Faculty.values());

    private final String description;

    public static List<FacultyDto> getFacultiesForWeb() {
        return FACULTIES
                .stream()
                .filter(faculty -> !faculty.isUnknown())
                .map(faculty -> new FacultyDto(faculty.name(), faculty.getDescription()))
                .toList();
    }

    //create method to parse text to enum here
    public static Faculty fromString(final String text) {
        return FACULTIES.stream()
                .filter(faculty -> faculty.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public boolean isUnknown() {
        return this.equals(UNKNOWN);
    }

    public static record FacultyDto(String name, String description) {

    }

}
