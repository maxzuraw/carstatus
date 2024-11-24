package com.carstatus.validators;

import com.carstatus.domain.CarRequest;
import com.carstatus.exceptions.InvalidRequestException;
import com.carstatus.services.RequestIdService;
import io.micronaut.context.annotation.Bean;
import io.micronaut.validation.validator.Validator;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@Bean
@RequiredArgsConstructor
public class CarRequestValidator {

    private final Validator validator;
    private final RequestIdService requestIdService;

    public void validate(CarRequest carRequest) {
        Set<ConstraintViolation<CarRequest>> violations = validator.validate(carRequest);
        if (violations.isEmpty()) {
            return;
        }
        List<String> collectedMessages = violations.stream().map(ConstraintViolation::getMessage).toList();
        String violationsCombined = String.join(",", collectedMessages);

        // NOTE: throw on purpose, so that GlobalExceptionHandler interacts, but we also don't want to duplicate same messages
        InvalidRequestException throwable = new InvalidRequestException(violationsCombined);
        String errorMsg = String.format("[%s] - %s", requestIdService.getTraceId(), violationsCombined);
        log.error(errorMsg, throwable);
        throw throwable;
    }

}
