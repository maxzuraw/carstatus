package com.carstatus.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopGarageResult {
    @JsonProperty("maintenance_frequency")
    private MaintenanceFrequency maintenanceFrequency;
}
