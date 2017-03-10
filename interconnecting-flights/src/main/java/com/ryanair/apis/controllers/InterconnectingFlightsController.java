package com.ryanair.apis.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ryanair.apis.resources.RyanairRouteResource;

@RestController
public class InterconnectingFlightsController {
	private static final String ROUTES_ENDPOINT = "https://api.ryanair.com/core/3/routes/";
	private static final String SCHEDULES_ENDPOINT = "https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}";

	private final RestTemplate restTemplate;

	@Autowired
	public InterconnectingFlightsController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/interconnections")
	public RyanairRouteResource[] routes(@RequestParam(value = "departure", required = true) String departure,
			@RequestParam(value = "arrival", required = true) String arrival,
			@RequestParam(value = "departureDateTime", required = true) String departureDateTime,
			@RequestParam(value = "arrivalDateTime", required = true) String arrivalDateTime) {

		RyanairRouteResource[] routes = restTemplate.getForObject(ROUTES_ENDPOINT, RyanairRouteResource[].class);

		List<List<String>> foundRoutes = new ArrayList<List<String>>();
		Properties matrix = new Properties();

		for (RyanairRouteResource route : routes) {
			if (departure.equalsIgnoreCase(route.getAirportFrom()) && arrival.equalsIgnoreCase(route.getAirportTo())) {
				List<String> zeroStopRoute = new ArrayList<String>(2);
				zeroStopRoute.add(departure);
				zeroStopRoute.add(arrival);
				foundRoutes.add(zeroStopRoute);
			} else if (departure.equalsIgnoreCase(route.getAirportFrom())
					&& !arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (matrix.containsKey(route.getAirportTo())) {
					List<String> oneStopRoute = new ArrayList<String>(3);
					oneStopRoute.add(departure);
					oneStopRoute.add(route.getAirportTo());
					oneStopRoute.add(arrival);
					foundRoutes.add(oneStopRoute);
				} else {
					matrix.put(route.getAirportTo(), departure);
				}
			} else if (!departure.equalsIgnoreCase(route.getAirportFrom())
					&& arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (matrix.containsKey(route.getAirportFrom())) {
					List<String> oneStopRoute = new ArrayList<String>(3);
					oneStopRoute.add(departure);
					oneStopRoute.add(route.getAirportFrom());
					oneStopRoute.add(arrival);
					foundRoutes.add(oneStopRoute);
				} else {
					matrix.put(route.getAirportFrom(), arrival);
				}
			}
		}

		return routes;
	}
}
