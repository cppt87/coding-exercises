package com.ryanair.apis.controllers;

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

		// ArrayNode resources = routesResponse.getBody();

		// StringBuilder builder = new StringBuilder();
		// for (int i = 0; i < resources.length; i++) {
		// builder.append("Ryanair serves a flight from
		// ").append(resources[i].getAirportFrom()).append(" to ")
		// .append(resources[i].getAirportTo()).append("<br />");
		// }

		// HttpResponse<Author> authorResponse =
		// Unirest.get("http://httpbin.org/books/{id}/author")
		// .routeParam("id", bookObject.getId())
		// .asObject(Author.class);
		//
		// Author authorObject = authorResponse.getBody();
		return restTemplate.getForObject(ROUTES_ENDPOINT, RyanairRouteResource[].class);
	}
}
