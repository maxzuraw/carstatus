package com.carstatus.adapters.topgarage;

import com.carstatus.adapters.CommonVins;
import com.carstatus.domain.MaintenanceFrequency;
import com.carstatus.domain.TopGarageResult;
import com.carstatus.exceptions.MaintenanceServiceFailedException;
import com.carstatus.services.RequestIdService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
// NOTE: set to 2s, for testing purpose in running application (sending one request with fake failure-triggering vin,
// and parallel sending second request with vin, which returns "correct" values)
@Retryable(delay = "2s")
@Singleton
@Primary
@RequiredArgsConstructor
@Requires(property = "carstatus.simulation.maintenance.enabled", value = "true")
public class TopGarageStub implements TopGarageOperations {

    private final RequestIdService requestIdService;
    @Override
    public Single<TopGarageResult> getMaintenanceFrequency(@NotBlank String vin) {
        log.info("[{}] - topgarage stub thread - {}", requestIdService.getTraceId(), Thread.currentThread().getName());
        if(CommonVins.FAILURE_CAUSING_VIN.equalsIgnoreCase(vin)) {
            throw new MaintenanceServiceFailedException("TopGarage's hardware is being stolen couple of days ago. Please be patient until we complete our tools again. Thank you!");
        }else if(CommonVins.REQUIREMENTS_SAMPLE_VIN.equalsIgnoreCase(vin)) {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(MaintenanceFrequency.MEDIUM)
                    .build());
        } else if( CommonVins.HTTP_CLIENT_RESPONSE_EXCEPTION_VIN.equalsIgnoreCase(vin)) {
            HttpClientResponseException exception = new HttpClientResponseException("Client http://stubgarage.com. Client (436)", HttpResponse.status(HttpStatus.UNAUTHORIZED));
            exception.setServiceId("http://stubgarage.com");
            throw exception;
        } else if(CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_POOR_VERY_LOW.equalsIgnoreCase(vin)) {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(MaintenanceFrequency.VERY_LOW)
                    .build());
        } else if(CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_POOR_LOW.equalsIgnoreCase(vin)) {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(MaintenanceFrequency.LOW)
                    .build());
        } else if(CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_AVERAGE_MEDIUM.equalsIgnoreCase(vin)) {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(MaintenanceFrequency.MEDIUM)
                    .build());
        } else if(CommonVins.VIN_SAMPLE_MAINTENANCE_SCORE_GOOD_HIGH.equalsIgnoreCase(vin)) {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(MaintenanceFrequency.HIGH)
                    .build());
        } else {
            return Single.just(TopGarageResult.builder()
                    .maintenanceFrequency(getRandomEnumValue(MaintenanceFrequency.class))
                    .build());
        }
    }

    private static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
