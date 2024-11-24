package com.carstatus.utils;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.Feature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarRequestUtils {

    public static boolean isFeatureMaintenancePresent(CarRequest request) {
        return request.getFeatures().contains(Feature.MAINTENANCE);
    }

    public static boolean isFeatureAccidentFreePresent(CarRequest request) {
        return request.getFeatures().contains(Feature.ACCIDENT_FREE);
    }
}
