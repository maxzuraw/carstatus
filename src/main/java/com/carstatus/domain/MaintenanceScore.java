package com.carstatus.domain;

import lombok.Getter;

@Getter
public enum MaintenanceScore {
    POOR("poor"),
    AVERAGE("average"),
    GOOD("good");

    private final String description;

    MaintenanceScore(String description) {
        this.description = description;
    }


}
