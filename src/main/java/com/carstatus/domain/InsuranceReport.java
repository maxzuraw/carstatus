package com.carstatus.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsuranceReport {
    private int claims;
}
