package com.carstatus.services;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.CarStatus;
import com.carstatus.domain.InsuranceResult;
import com.carstatus.domain.TopGarageResult;
import com.carstatus.utils.MaintenanceScoreUtils;
import io.micronaut.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.carstatus.utils.CarRequestUtils.isFeatureAccidentFreePresent;
import static com.carstatus.utils.CarRequestUtils.isFeatureMaintenancePresent;

@Slf4j
@Bean
@RequiredArgsConstructor
public class CarService {

    private final InsuranceService insuranceService;
    private final MaintenanceService maintenanceService;
    private final RequestIdService requestIdService;

    public Mono<CarStatus> get(CarRequest request) {
        Mono<InsuranceResult> insuranceCallMono = insuranceService.getInsuranceCallMono(request);
        Mono<TopGarageResult> maintanenceCallMono = maintenanceService.getMaintanenceCallMono(request);

        return Flux.zip(insuranceCallMono, maintanenceCallMono)
                .map(tuple -> {
                    InsuranceResult insuranceResult = tuple.getT1();
                    TopGarageResult maintenanceResult = tuple.getT2();

                    CarStatus.CarStatusBuilder resultBuilder = CarStatus.builder()
                            .requestId(requestIdService.getTraceId())
                            .vin(request.getVin());

                    if (includeAccidentFree(request, insuranceResult)) {
                        resultBuilder.accidentFree(insuranceResult.getReport().getClaims() <= 0);
                    }

                    if (includeMaintenanceScore(request, maintenanceResult)) {
                        resultBuilder.maintenanceScore(MaintenanceScoreUtils.toScore(maintenanceResult.getMaintenanceFrequency()).getDescription());
                    }

                    return resultBuilder.build();
                })
                .single();
    }

    private static boolean includeAccidentFree(CarRequest request, InsuranceResult insuranceResult) {
        return isFeatureAccidentFreePresent(request) && null != insuranceResult.getReport();
    }

    private static boolean includeMaintenanceScore(CarRequest request, TopGarageResult maintenanceResult) {
        return isFeatureMaintenancePresent(request) && null != maintenanceResult.getMaintenanceFrequency();
    }

}
