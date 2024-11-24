package com.carstatus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Feature {
    ACCIDENT_FREE,
    MAINTENANCE;

    @JsonCreator
    public static Feature forValue(String value) {
        if (null == value) {
            throw new IllegalArgumentException();
        }
        return Arrays.stream(Feature.values())
                .filter(feature -> feature.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
