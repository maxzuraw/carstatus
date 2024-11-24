package com.carstatus.services;

import com.carstatus.adapters.topgarage.TopGarageOperations;
import com.carstatus.domain.CarRequest;
import com.carstatus.domain.TopGarageResult;
import com.carstatus.exceptions.MaintenanceServiceFailedException;
import io.micronaut.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

import static com.carstatus.utils.CarRequestUtils.isFeatureMaintenancePresent;

@Slf4j
@Bean
@RequiredArgsConstructor
public class MaintenanceService {

    private final TopGarageOperations maintenanceOperations;
    private final RequestIdService requestIdService;

    public Mono<TopGarageResult> getMaintanenceCallMono(CarRequest request) {
        return Mono.fromCallable(getTopGarageResultCallable(request))
                .onErrorResume(MaintenanceServiceFailedException.class, error -> {
                    String errorMsg = String.format("[%s] - Maintenance callable failed.", requestIdService.getTraceId());
                    log.error(errorMsg, error);
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic()) // NOTE: make sure it's in separate thread
                .defaultIfEmpty(TopGarageResult.builder().build());
    }

    private Callable<TopGarageResult> getTopGarageResultCallable(CarRequest request) {
        // NOTE: only if feature maintenance is present call 3rd party service, otherwise null
        return () -> isFeatureMaintenancePresent(request) ? maintenanceOperations.getMaintenanceFrequency(request.getVin()).blockingGet() : null;
    }
}
