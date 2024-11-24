package com.carstatus.services;

import brave.Tracing;
import io.micronaut.context.annotation.Bean;
import lombok.RequiredArgsConstructor;

@Bean
@RequiredArgsConstructor
public class RequestIdService {

    private final Tracing tracing;

    public String getTraceId() {
        return tracing.tracer().currentSpan().context().traceIdString();
    }
}
