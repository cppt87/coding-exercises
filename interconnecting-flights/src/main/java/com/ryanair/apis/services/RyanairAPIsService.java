package com.ryanair.apis.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.models.RyanairScheduleResource;

@Service("ryanairService")
public class RyanairAPIsService implements IRyanairAPIsService {
	private static final String ROUTES_ENDPOINT = "https://api.ryanair.com/core/3/routes/";
	private static final String SCHEDULES_ENDPOINT = "https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}";
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public RyanairRouteResource[] routesAPI() {
		return restTemplate.getForObject(ROUTES_ENDPOINT, RyanairRouteResource[].class);
	}

	@Override
	public RyanairScheduleResource schedulesAPI(String departure, String arrival, int year, int month) {
		Map<String, String> params = new HashMap<String, String>(4);
		params.put("departure", departure);
		params.put("arrival", arrival);
		params.put("year", String.valueOf(year));
		params.put("month", String.valueOf(month));
		
		// CHECK RESPONSE BEFORE DESERIALIZE!!!
		return restTemplate.getForObject(SCHEDULES_ENDPOINT, RyanairScheduleResource.class, params);
	}
}
