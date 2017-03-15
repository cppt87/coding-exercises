package com.ryanair.apis;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.annotation.RequestScope;

import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.models.RyanairScheduleResource;
import com.ryanair.apis.models.SolutionResource;
import com.ryanair.apis.services.IRyanairAPIsService;

@SpringBootApplication
public class InterconnectingFlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterconnectingFlightsApplication.class, args);
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
	public RyanairRouteResource[] routes(IRyanairAPIsService ryanairService) throws Exception {
		return ryanairService.routesAPI();
	}

	@Bean
	@RequestScope
	public Deque<SolutionResource> solutions() {
		return new ArrayDeque<SolutionResource>();
	}
}