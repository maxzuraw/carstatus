package com.carstatus.validators;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.Feature;
import com.carstatus.exceptions.InvalidRequestException;
import com.carstatus.services.RequestIdService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class CarRequestValidatorTest {


    @Inject
    Validator validator;

    @Inject
    RequestIdService requestIdService;

    @Test
    public void itShouldThrowExceptionBecauseFieldsAreNotFilled() {
        // arrange

        CarRequest request = CarRequest.builder()
                .features(Collections.emptyList())
                .build();

        // act

        try {
            CarRequestValidator carRequestValidator = new CarRequestValidator(validator, requestIdService);
            carRequestValidator.validate(request);
        } catch(InvalidRequestException ex) {
            // assert
            assertEquals("vin cannot be null,features list cannot be empty,features size must be between 1 and 2", ex.getMessage());
        }

    }

    @Test
    public void itShouldNotThrowException() {

        try (MockedStatic<LoggerFactory> mockedLoggerFactory = Mockito.mockStatic(LoggerFactory.class)) {
            Logger mockedLogger = Mockito.mock(Logger.class);
            mockedLoggerFactory
                    .when(() -> LoggerFactory.getLogger(CarRequestValidator.class))
                    .thenReturn(mockedLogger);

            // arrange

            CarRequest request = CarRequest.builder()
                    .vin("ABC")
                    .features(List.of(Feature.MAINTENANCE))
                    .build();

            // act

            CarRequestValidator carRequestValidator = new CarRequestValidator(validator, requestIdService);
            carRequestValidator.validate(request);

            // assert

            Mockito.verify(mockedLogger, Mockito.times(0)).atError();
        }
    }
}
