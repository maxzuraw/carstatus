package com.carstatus.adapters.insurance;

import com.carstatus.domain.InsuranceResult;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.NotBlank;

// NOTE: set to 2s, for testing purpose in running application (sending one request with fake failure-triggering vin,
// and parallel sending second request with vin, which returns "correct" values)
@Retryable(delay = "2s")
@Client("https://insurance.com/accidents")
public interface InsuranceClient extends InsuranceOperations {

    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Get("/report")
    @ContinueSpan
    Single<InsuranceResult> getInsuranceReport(@NotBlank  @Parameter("vin") String vin);
}
