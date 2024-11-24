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
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.carstatus.TestCommons.STUBBED_REQUEST_ID;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_INSURANCE_FREE_TRUE;
import static com.carstatus.domain.Feature.ACCIDENT_FREE;
import static com.carstatus.domain.Feature.MAINTENANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class CarControllerBothServicesTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void itShouldReturnBothServicesResultObject() {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(VIN_SAMPLE_INSURANCE_FREE_TRUE)
                .features(List.of(ACCIDENT_FREE, MAINTENANCE))
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
        assertEquals(VIN_SAMPLE_INSURANCE_FREE_TRUE, carStatus.getVin());
        assertEquals(STUBBED_REQUEST_ID, carStatus.getRequestId());
        assertEquals(MaintenanceScore.GOOD.getDescription(), carStatus.getMaintenanceScore());
    }

    @Test
    void itShouldReturnBothServicesResultJson() {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(VIN_SAMPLE_INSURANCE_FREE_TRUE)
                .features(List.of(ACCIDENT_FREE, MAINTENANCE))
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
        assertEquals("{\"vin\":\"4Y1SL65848Z411430\",\"request_id\":\"b75704d93eae31f8\",\"accident_free\":true,\"maintenance_score\":\"good\"}", responseBody);
    }
}
