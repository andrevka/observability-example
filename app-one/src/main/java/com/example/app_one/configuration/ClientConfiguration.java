package com.example.app_one.configuration;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfiguration {

    @Bean
    RestTemplate appTwoRestTemplate(RestTemplateBuilder restTemplateBuilder, @Value("${app-two-url}") String appTwoUrl) {
        return restTemplateBuilder.rootUri(appTwoUrl)
                .build();
    }

    @Bean
    RestTemplate appTwoRestTemplateWithManualSettings(@Value("${app-two-url}") String appTwoUrl, ObservationRegistry observationRegistry) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(appTwoUrl)
                .build();
        restTemplate.setObservationRegistry(observationRegistry);
        return restTemplate;
    }

    @Bean
    RestTemplate appTwoRestTemplateWithoutObservationRegistry(@Value("${app-two-url}") String appTwoUrl) {
        return new RestTemplateBuilder()
                .rootUri(appTwoUrl)
                .build();
    }

}
