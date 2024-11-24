package com.carstatus.adapters.insurance;

import com.carstatus.domain.InsuranceResult;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.NotBlank;

public interface InsuranceOperations {

    Single<InsuranceResult> getInsuranceReport(@NotBlank String vin);
}
