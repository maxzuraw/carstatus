package com.carstatus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MaintenanceFrequency {
    VERY_LOW("very-low"),
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    final String description;

    MaintenanceFrequency(String description) {
        this.description = description;
    }

    @JsonCreator
    public static MaintenanceFrequency fromDescription(String value) {
        if (null == value) {
            throw new IllegalArgumentException();
        }
        return Arrays.stream(MaintenanceFrequency.values())
                .filter(mf -> mf.getDescription().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
