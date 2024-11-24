package com.carstatus.utils;

import com.carstatus.domain.CarRequest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.carstatus.domain.Feature.ACCIDENT_FREE;
import static com.carstatus.domain.Feature.MAINTENANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class CarRequestUtilsTest {

    private static Stream<Arguments> maintenenceConfirmationParameters() {
        return Stream.of(
                Arguments.of(CarRequest.builder().features(List.of(ACCIDENT_FREE)).build(), false),
                Arguments.of(CarRequest.builder().features(List.of(ACCIDENT_FREE, MAINTENANCE)).build(), true),
                Arguments.of(CarRequest.builder().features(List.of(MAINTENANCE)).build(), true)
        );
    }
    @ParameterizedTest
    @MethodSource("maintenenceConfirmationParameters")
    public void itShouldConfirmMaintenanceIsNotPresent(CarRequest inputCarRequest, boolean expectedResult) {
        boolean result = CarRequestUtils.isFeatureMaintenancePresent(inputCarRequest);

        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> accidentFreeConfirmationParameters() {
        return Stream.of(
                Arguments.of(CarRequest.builder().features(List.of(MAINTENANCE)).build(), false),
                Arguments.of(CarRequest.builder().features(List.of(MAINTENANCE, ACCIDENT_FREE)).build(), true),
                Arguments.of(CarRequest.builder().features(List.of(ACCIDENT_FREE)).build(), true)
        );
    }
    @ParameterizedTest
    @MethodSource("accidentFreeConfirmationParameters")
    public void itShouldConfirmOrNotThatAccidentFreeIsPresent(CarRequest inputCarRequest, boolean expectedResult) {
        boolean result = CarRequestUtils.isFeatureAccidentFreePresent(inputCarRequest);

        assertEquals(expectedResult, result);
    }

}
