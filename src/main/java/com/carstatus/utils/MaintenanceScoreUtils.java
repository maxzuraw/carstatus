package com.carstatus.utils;

import com.carstatus.domain.MaintenanceFrequency;
import com.carstatus.domain.MaintenanceScore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.carstatus.domain.MaintenanceScore.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaintenanceScoreUtils {

    public static MaintenanceScore toScore(MaintenanceFrequency frequency) {
        return switch(frequency) {
            case LOW, VERY_LOW -> POOR;
            case MEDIUM -> AVERAGE;
            case HIGH -> GOOD;
        };
    }
}
