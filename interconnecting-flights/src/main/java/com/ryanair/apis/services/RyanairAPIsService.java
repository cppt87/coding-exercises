package com.ryanair.apis.services;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.apis.exceptions.ServiceUnavailableException;
import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.models.RyanairScheduleResource;
import com.ryanair.apis.utils.InterconnectingFlightsUtils;

@Service("ryanairService")
public class RyanairAPIsService implements IRyanairAPIsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RyanairAPIsService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public RyanairRouteResource[] routesAPI() throws Exception {
		try {
			String json = restTemplate.getForObject(InterconnectingFlightsUtils.ROUTES_ENDPOINT, String.class);
			LOGGER.debug("Ryanair Routes API results:\n{}", json);
			// try to convert JSON string to RyanairRouteResource[]
			return this.mapper.readValue(json, new TypeReference<RyanairRouteResource[]>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceUnavailableException("Ryanair Routes API", e);
		}
	}

	@Override
	public RyanairScheduleResource schedulesAPI(String departure, String arrival, int year, int month)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>(4);
		params.put("departure", departure);
		params.put("arrival", arrival);
		params.put("year", String.valueOf(year));
		params.put("month", String.valueOf(month));
		try {
			ResponseEntity<String> entity = restTemplate.exchange(InterconnectingFlightsUtils.SCHEDULES_ENDPOINT, HttpMethod.GET, null,
					String.class, params);
			LOGGER.info("Ryanair Schedules API result code {} with parameters {}", entity.getStatusCodeValue(), params);
			switch (entity.getStatusCode()) {
			case OK:
			case CREATED:
				LOGGER.debug("Ryanair Schedules API result:\n{}", entity.getBody());
				return this.mapper.readValue(entity.getBody(), new TypeReference<RyanairScheduleResource>() {
				});
			case NOT_FOUND:
				RyanairScheduleResource emptyResource = new RyanairScheduleResource();
				emptyResource.setMonth(month);
				emptyResource.setDays(new LinkedHashSet<>(0));
				return emptyResource;
			default:
				throw new ServiceUnavailableException("Ryanair Schedules API", new Throwable(entity.getBody()));
			}
		} catch (HttpClientErrorException e) {
			switch (e.getStatusCode()) {
			case NOT_FOUND:
				RyanairScheduleResource emptyResource = new RyanairScheduleResource();
				emptyResource.setMonth(month);
				emptyResource.setDays(new LinkedHashSet<>(0));
				return emptyResource;
			default:
				throw new ServiceUnavailableException("Ryanair Schedules API", e);
			}			
		} catch (Exception e) {
			throw new ServiceUnavailableException("Ryanair Schedules API", e);
		}
	}
}
