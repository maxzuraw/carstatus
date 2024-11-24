package com.carstatus.controller;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.CarStatus;
import com.carstatus.services.CarService;
import com.carstatus.services.RequestIdService;
import com.carstatus.validators.CarRequestValidator;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.tracing.annotation.NewSpan;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/check")
@RequiredArgsConstructor
public class CarController {

    private final RequestIdService requestIdService;
    private final CarRequestValidator requestValidator;
    private final CarService carService;

    @Post(uri="/", produces= MediaType.APPLICATION_JSON)
    @NewSpan("car_checker")
    public Mono<CarStatus> check(@Nonnull @Body CarRequest carRequest) {
        log.info("[{}] - controller thread - {}", requestIdService.getTraceId(), Thread.currentThread().getName());
        requestValidator.validate(carRequest);
        return carService.get(carRequest);
    }

}