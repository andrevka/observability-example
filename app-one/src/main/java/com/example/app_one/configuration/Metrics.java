package com.example.app_one.configuration;

import com.example.app_one.repository.UserSessionsRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Metrics {

	private final UserSessionsRepository userSessionsRepository;

	private final MeterRegistry meterRegistry;

	@Bean
	Counter methodThreeCounter() {
		return meterRegistry.counter("method_three_counter");
	}

	@Bean
	Gauge userSessionsGauge() {
		return Gauge.builder("user_sessions", userSessionsRepository::getActiveUsers).register(meterRegistry);
	}

}
