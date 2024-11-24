package com.carstatus.services;

import com.carstatus.adapters.insurance.InsuranceOperations;
import com.carstatus.domain.CarRequest;
import com.carstatus.domain.InsuranceResult;
import com.carstatus.exceptions.InsuranceServiceFailedException;
import io.micronaut.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

import static com.carstatus.utils.CarRequestUtils.isFeatureAccidentFreePresent;

@Slf4j
@Bean
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceOperations insuranceOperations;
    private final RequestIdService requestIdService;

    public Mono<InsuranceResult> getInsuranceCallMono(CarRequest request) {
        return Mono.fromCallable(getInsuranceResultCallable(request))
                .onErrorResume(InsuranceServiceFailedException.class, error -> {
                    String errorMsg = String.format("[%s] - Insurance callable failed.", requestIdService.getTraceId());
                    log.error(errorMsg, error);
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic()) // NOTE: make sure it's in separate thread
                .defaultIfEmpty(InsuranceResult.builder().build());
    }

    private Callable<InsuranceResult> getInsuranceResultCallable(CarRequest request) {
        // NOTE: only if feature accident_free is present call 3rd party service, otherwise null
        return () -> isFeatureAccidentFreePresent(request) ? insuranceOperations.getInsuranceReport(request.getVin()).blockingGet() : null;
    }
}
