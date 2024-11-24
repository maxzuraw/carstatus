package com.carstatus.adapters.insurance;

import com.carstatus.adapters.CommonVins;
import com.carstatus.domain.InsuranceReport;
import com.carstatus.domain.InsuranceResult;
import com.carstatus.exceptions.InsuranceServiceFailedException;
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

import static com.carstatus.adapters.CommonVins.FAILURE_CAUSING_VIN;
import static com.carstatus.adapters.CommonVins.REQUIREMENTS_SAMPLE_VIN;

@Slf4j
// NOTE: set to 2s, for testing purpose in running application (sending one request with fake failure-triggering vin,
// and parallel sending second request with vin, which returns "correct" values)
@Retryable(delay = "2s")
@Singleton
@Primary
@RequiredArgsConstructor
@Requires(property = "carstatus.simulation.insurance.enabled", value = "true")
public class InsuranceStub implements InsuranceOperations {

    private final RequestIdService requestIdService;

    @Override
    public Single<InsuranceResult> getInsuranceReport(@NotBlank String vin) {
        log.info("[{}] - insurance stub thread - {}", requestIdService.getTraceId(), Thread.currentThread().getName());
        if (FAILURE_CAUSING_VIN.equalsIgnoreCase(vin)) {
            throw new InsuranceServiceFailedException("Our services are being hacked. Please visit us some other time. Thank you.");
        } else if (REQUIREMENTS_SAMPLE_VIN.equalsIgnoreCase(vin)) {
            return Single.just(InsuranceResult.builder()
                    .report(InsuranceReport.builder().claims(3).build())
                    .build());
        } else if (CommonVins.HTTP_CLIENT_RESPONSE_EXCEPTION_VIN.equalsIgnoreCase(vin)) {
            HttpClientResponseException exception = new HttpClientResponseException("You shall not pass (403)", HttpResponse.status(HttpStatus.FORBIDDEN));
            exception.setServiceId("http://stubinsurance.com");
            throw exception;
        } else if(CommonVins.VIN_SAMPLE_INSURANCE_FREE_TRUE.equalsIgnoreCase(vin)) {
            return Single.just(InsuranceResult.builder()
                    .report(InsuranceReport.builder().claims(0).build())
                    .build());
        } else if (CommonVins.VIN_SAMPLE_INSURANCE_FREE_FALSE.equalsIgnoreCase(vin)) {
            return Single.just(InsuranceResult.builder()
                    .report(InsuranceReport.builder().claims(4).build())
                    .build());
        } else {
            int randomNumber = ThreadLocalRandom.current().nextInt(-200, 200);
            return Single.just(InsuranceResult.builder()
                    .report(InsuranceReport.builder().claims(randomNumber).build())
                    .build());
        }

    }
}
