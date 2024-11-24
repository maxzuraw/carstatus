package com.carstatus.services;

import com.carstatus.domain.CarRequest;
import com.carstatus.domain.Feature;
import com.carstatus.domain.TopGarageResult;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@MicronautTest
public class MaintenanceServiceTest {

    @Inject
    MaintenanceService maintenanceService;

    @Test
    public void itShouldRunWithBoundedElasticThread() {
        // arrange

        CarRequest request = CarRequest.builder().vin("123").features(List.of(Feature.MAINTENANCE)).build();
        AtomicReference<String> threadName = new AtomicReference<>();

        // act

        Mono<TopGarageResult> result = maintenanceService.getMaintanenceCallMono(request)
                .doOnNext($ -> threadName.set(Thread.currentThread().getName()));

        // assert

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();

        assert threadName.get() != null && threadName.get().contains("boundedElastic");
    }
}
