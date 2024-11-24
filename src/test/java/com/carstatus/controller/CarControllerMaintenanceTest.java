package com.carstatus.controller;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.CarStatus;
import com.carstatus.domain.MaintenanceScore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.carstatus.TestCommons.STUBBED_REQUEST_ID;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_AVERAGE_MEDIUM;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_GOOD_HIGH;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_POOR_LOW;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_POOR_VERY_LOW;
import static com.carstatus.domain.Feature.MAINTENANCE;
import static com.carstatus.domain.MaintenanceScore.AVERAGE;
import static com.carstatus.domain.MaintenanceScore.GOOD;
import static com.carstatus.domain.MaintenanceScore.POOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
public class CarControllerMaintenanceTest {

    @Inject
    @Client("/")
    HttpClient client;

    private static Stream<Arguments> maintenanceParametersForObjectTests() {
        return Stream.of(
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_POOR_VERY_LOW, POOR),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_POOR_LOW, POOR),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_AVERAGE_MEDIUM, AVERAGE),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_GOOD_HIGH, GOOD)
        );
    }
    @ParameterizedTest
    @MethodSource("maintenanceParametersForObjectTests")
    public void itShouldReturnMaintenanceScoreForMaintenanceFrequencyInObjectTest(String inputVin, MaintenanceScore expectedScore) {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(inputVin)
                .features(List.of(MAINTENANCE))
                .build();

        // act

        HttpResponse<CarStatus> result = client.toBlocking().exchange(
                HttpRequest.POST("/check", request),
                CarStatus.class
        );

        // assert

        assertEquals(200, result.code());
        CarStatus carStatus = result.body();
        assertNotNull(carStatus);
        assertEquals(inputVin, carStatus.getVin());
        assertEquals(STUBBED_REQUEST_ID, carStatus.getRequestId());
        assertEquals(expectedScore.getDescription(), carStatus.getMaintenanceScore());

        assertNull(carStatus.getAccidentFree());
    }

    private static Stream<Arguments> maintenanceParametersForJsonTests() {
        return Stream.of(
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_POOR_VERY_LOW, "{\"vin\":\"4Y1SL65848Z411432\",\"request_id\":\"b75704d93eae31f8\",\"maintenance_score\":\"poor\"}"),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_POOR_LOW, "{\"vin\":\"4Y1SL65848Z411433\",\"request_id\":\"b75704d93eae31f8\",\"maintenance_score\":\"poor\"}"),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_AVERAGE_MEDIUM, "{\"vin\":\"4Y1SL65848Z411434\",\"request_id\":\"b75704d93eae31f8\",\"maintenance_score\":\"average\"}"),
                Arguments.of(VIN_SAMPLE_MAINTENANCE_SCORE_GOOD_HIGH, "{\"vin\":\"4Y1SL65848Z411430\",\"request_id\":\"b75704d93eae31f8\",\"maintenance_score\":\"good\"}")
        );
    }
    @ParameterizedTest
    @MethodSource("maintenanceParametersForJsonTests")
    public void itShouldReturnMaintenanceScoreForMaintenanceFrequencyInJsonTest(String inputVin, String expectedJson) {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(inputVin)
                .features(List.of(MAINTENANCE))
                .build();

        // act

        HttpResponse<String> result = client.toBlocking().exchange(
                HttpRequest.POST("/check", request),
                String.class
        );

        // assert

        assertEquals(200, result.code());

        String responseBody = result.body();
        assertNotNull(responseBody);
        assertEquals(expectedJson, responseBody);
    }

}
