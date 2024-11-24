package com.carstatus.utils;

import com.carstatus.domain.MaintenanceFrequency;
import com.carstatus.domain.MaintenanceScore;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.carstatus.domain.MaintenanceFrequency.HIGH;
import static com.carstatus.domain.MaintenanceFrequency.LOW;
import static com.carstatus.domain.MaintenanceFrequency.MEDIUM;
import static com.carstatus.domain.MaintenanceFrequency.VERY_LOW;
import static com.carstatus.domain.MaintenanceScore.AVERAGE;
import static com.carstatus.domain.MaintenanceScore.GOOD;
import static com.carstatus.domain.MaintenanceScore.POOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class MaintenanceScoreUtilsTest {

    private static Stream<Arguments> frequencyToScore() {
        return Stream.of(
                Arguments.of(VERY_LOW, POOR),
                Arguments.of(LOW, POOR),
                Arguments.of(MEDIUM, AVERAGE),
                Arguments.of(HIGH, GOOD)
        );
    }
    @ParameterizedTest
    @MethodSource("frequencyToScore")
    public void itShouldScorePoorForVeryLowFrequency(MaintenanceFrequency frequency, MaintenanceScore expectedScore) {
        MaintenanceScore result = MaintenanceScoreUtils.toScore(frequency);
        assertEquals(expectedScore, result);
    }
}
