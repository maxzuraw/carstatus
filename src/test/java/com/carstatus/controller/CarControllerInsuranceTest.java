package com.carstatus.controller;

import brave.Tracing;
import com.carstatus.domain.CarRequest;
import com.carstatus.domain.CarStatus;
import com.carstatus.services.RequestIdService;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.carstatus.TestCommons.STUBBED_REQUEST_ID;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_INSURANCE_FREE_FALSE;
import static com.carstatus.adapters.CommonVins.VIN_SAMPLE_INSURANCE_FREE_TRUE;
import static com.carstatus.domain.Feature.ACCIDENT_FREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class CarControllerInsuranceTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Singleton
    @Replaces(RequestIdService.class)
    static class MockRequestIdService extends RequestIdService {

        public MockRequestIdService(Tracing tracing) {
            super(tracing);
        }

        @Override
        public String getTraceId() {
            return STUBBED_REQUEST_ID;
        }
    }

    private static Stream<Arguments> objectParameters() {
        return Stream.of(
                Arguments.of(VIN_SAMPLE_INSURANCE_FREE_TRUE, Boolean.TRUE),
                Arguments.of(VIN_SAMPLE_INSURANCE_FREE_FALSE, Boolean.FALSE)
        );
    }
    @ParameterizedTest
    @MethodSource("objectParameters")
    void itShouldReturnAccidentFreeObject(String inputVin, Boolean expectedAccidentFree) {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(inputVin)
                .features(List.of(ACCIDENT_FREE))
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
        assertEquals(expectedAccidentFree, carStatus.getAccidentFree());
    }

    private static Stream<Arguments> jsonParameters() {
        return Stream.of(
                Arguments.of(VIN_SAMPLE_INSURANCE_FREE_TRUE, "{\"vin\":\"4Y1SL65848Z411430\",\"request_id\":\"b75704d93eae31f8\",\"accident_free\":true}"),
                Arguments.of(VIN_SAMPLE_INSURANCE_FREE_FALSE, "{\"vin\":\"4Y1SL65848Z411431\",\"request_id\":\"b75704d93eae31f8\",\"accident_free\":false}")
        );
    }
    @ParameterizedTest
    @MethodSource("jsonParameters")
    void itShouldReturnAccidentFreeJson(String inputVin, String expectedJson) {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(inputVin)
                .features(List.of(ACCIDENT_FREE))
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
