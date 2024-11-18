package com.example.app_one.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Metrics {

    private final MeterRegistry meterRegistry;

    @Bean
    Counter methodThreeCounter() {
        return meterRegistry.counter("method_three_counter");
    }

}
