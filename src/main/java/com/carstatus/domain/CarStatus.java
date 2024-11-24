package com.carstatus.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarStatus {
    @JsonProperty("request_id")
    private String requestId;
    private String vin;
    @JsonProperty("accident_free")
    private Boolean accidentFree;
    @JsonProperty("maintenance_score")
    private String maintenanceScore;
}
