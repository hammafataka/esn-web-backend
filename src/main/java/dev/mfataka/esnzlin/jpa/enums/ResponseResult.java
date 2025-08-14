package dev.mfataka.esnzlin.jpa.enums;

import java.util.Arrays;
import java.util.List;

public enum ResponseResult {

    OK,

    ERROR;

    public final static List<ResponseResult> RESPONSE_RESULTS = Arrays.asList(ResponseResult.values());

    public static ResponseResult fromString(String responseResult) {
        return RESPONSE_RESULTS
                .stream()
                .filter(event -> event.name().equalsIgnoreCase(responseResult))
                .findFirst()
                .orElse(ERROR);
    }
}
