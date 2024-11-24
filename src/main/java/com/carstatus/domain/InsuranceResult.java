package com.carstatus.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsuranceResult {
    private InsuranceReport report;
}
