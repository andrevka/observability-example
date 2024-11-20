package com.example.app_one.service;

import com.example.app_one.controller.model.Status;
import com.example.app_one.repository.UserSessionsRepository;
import com.example.app_one.service.exception.ExceptionWhileObserved;
import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.core.instrument.Counter;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppOneService {

	private final RestTemplate appTwoRestTemplate;

	private final RestTemplate appTwoRestTemplateWithManualSettings;

	private final RestTemplate appTwoRestTemplateWithoutObservationRegistry;

	private final UserSessionsRepository userSessionsRepository;

	private final Counter methodThreeCounter;

	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

	private static final ExecutorService wrappedExecutorService = ContextExecutorService
		.wrap(Executors.newFixedThreadPool(5), ContextSnapshot::captureAll);

	private final Tracer tracer;

	public void successfulCall() {
		log.info("Calling appTwo /one");
		appTwoRestTemplate.getForEntity("/one", Void.class);
	}

	public void exceptionThrown() {
		log.info("Calling appTwo /two");
		appTwoRestTemplate.getForEntity("/two", Void.class);
	}

	public void incrementCounter() {
		log.info("Increment");
		methodThreeCounter.increment();
	}

	@Observed(name = "observed", contextualName = "observed")
	public void iAmTheObservedMethod(Status status) {
		log.info("Observed");
		switch (status) {
			case FAIL -> throw new ExceptionWhileObserved();
			case SUCCESS -> appTwoRestTemplate.getForEntity("/four", Void.class);
		}
	}

	public void runOnDifferentThreadWontPropagateTrace() {
		executorService.submit(() -> appTwoRestTemplate.getForEntity("/runOnDifferentThread", Void.class));
	}

	public void runOnDifferentThread() {
		Span span = tracer.currentSpan();
		executorService.submit(() -> {
			try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
				appTwoRestTemplate.getForEntity("/runOnDifferentThread", Void.class);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public void runOnDifferentThreadWrappedExecutorService() {
		wrappedExecutorService.submit(() -> appTwoRestTemplate.getForEntity("/runOnDifferentThread", Void.class));
	}

	public void appTwoRestTemplateWithManualSettings() {
		log.info("appTwoRestTemplateWithManualSettings");
		appTwoRestTemplateWithManualSettings.getForEntity("/five", Void.class);
	}

	public void appTwoRestTemplateWithoutObservationRegistry() {
		log.info("appTwoRestTemplateWithoutObservationRegistry");
		appTwoRestTemplateWithoutObservationRegistry.getForEntity("/six", Void.class);
	}

	public void setActiveUsers(Integer activeUsers) {
		log.info("Setting active users count {}", activeUsers);
		userSessionsRepository.setActiveUsers(activeUsers);
	}

}
