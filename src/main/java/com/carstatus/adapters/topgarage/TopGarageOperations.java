package com.carstatus.adapters.topgarage;

import com.carstatus.domain.TopGarageResult;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.NotBlank;

public interface TopGarageOperations {

    Single<TopGarageResult> getMaintenanceFrequency(@NotBlank String vin);
}
