package com.carstatus.controller;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.Feature;
import com.carstatus.errorhandlers.ErrorMessage;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.carstatus.adapters.CommonVins.HTTP_CLIENT_RESPONSE_EXCEPTION_VIN;
import static com.carstatus.adapters.CommonVins.REQUIREMENTS_SAMPLE_VIN;
import static com.carstatus.domain.Feature.ACCIDENT_FREE;
import static com.carstatus.domain.Feature.MAINTENANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class CarControllerErrorTest {

    @Inject
    @Client("/")
    HttpClient client;


    private static Stream<Arguments> errorsParameters() {
        return Stream.of(
                Arguments.of(null, List.of(MAINTENANCE, ACCIDENT_FREE), HttpStatus.BAD_REQUEST, "vin cannot be null"),
                Arguments.of(REQUIREMENTS_SAMPLE_VIN, null, HttpStatus.BAD_REQUEST, "features list cannot be empty"),
                Arguments.of(REQUIREMENTS_SAMPLE_VIN, List.of(MAINTENANCE, MAINTENANCE, ACCIDENT_FREE, ACCIDENT_FREE), HttpStatus.BAD_REQUEST, "features size must be between 1 and 2"),
                Arguments.of(REQUIREMENTS_SAMPLE_VIN, Collections.emptyList(), HttpStatus.BAD_REQUEST, "features list cannot be empty"),
                Arguments.of(HTTP_CLIENT_RESPONSE_EXCEPTION_VIN, List.of(ACCIDENT_FREE), HttpStatus.FORBIDDEN, "Call to [http://stubinsurance.com] failed. Please contact your administrator."),
                Arguments.of(HTTP_CLIENT_RESPONSE_EXCEPTION_VIN, List.of(MAINTENANCE), HttpStatus.UNAUTHORIZED, "Call to [http://stubgarage.com] failed. Please contact your administrator.")
        );
    }
    @ParameterizedTest
    @MethodSource("errorsParameters")
    void testErrors(String inputVin, List<Feature> inputFeatures, HttpStatus expectedStatus, String expectedErrorMsg) {
        // arrange

        CarRequest request = CarRequest.builder()
                .vin(inputVin)
                .features(inputFeatures)
                .build();

        // act

        var error = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(
                    HttpRequest.POST("/check", request),
                    ErrorMessage.class
            );
        });

        // assert
        assertNotNull(error);
        assertEquals(expectedStatus, error.getStatus());
        assertEquals(expectedErrorMsg, error.getMessage());
    }

}
