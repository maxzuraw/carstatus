package com.carstatus.domain;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Introspected
@Data
@Builder
public class CarRequest {
    @NotNull(message = "vin cannot be null")
    private String vin;
    @NotEmpty(message = "features list cannot be empty")
    @Size(min=1, max=2, message = "features size must be between 1 and 2")
    private List<Feature> features;
}
