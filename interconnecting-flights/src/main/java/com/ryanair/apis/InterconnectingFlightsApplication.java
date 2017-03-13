package com.ryanair.apis;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import com.ryanair.apis.models.RyanairScheduleResource;
import com.ryanair.apis.models.SolutionResource;

@SpringBootApplication
public class InterconnectingFlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterconnectingFlightsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Bean
	public Map<String, RyanairScheduleResource> cache() {
		return new HashMap<String, RyanairScheduleResource>();
	}

	@Bean
	public Stack<SolutionResource> solutions() {
		return new Stack<SolutionResource>();
	}
}
