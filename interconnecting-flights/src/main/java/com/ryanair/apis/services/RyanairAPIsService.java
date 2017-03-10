package com.ryanair.apis.services;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Size;

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
	public RyanairScheduleResource[] schedulesAPI(
			@Size(max = 3, min = 3, message = "departure must be 3 characters long") String departure,
			@Size(max = 3, min = 3, message = "arrival must be 3 characters long") String arrival,
			@Size(max = 4, min = 4, message = "year must be 3 characters long") String year,
			@Size(max = 1, min = 1, message = "month must be 1 character long") String month) {
		Map<String, String> params = new HashMap<String, String>(4);
		params.put("departure", departure);
		params.put("arrival", arrival);
		params.put("year", year);
		params.put("month", month);
		return restTemplate.getForObject(SCHEDULES_ENDPOINT, RyanairScheduleResource[].class, params);
	}
}
