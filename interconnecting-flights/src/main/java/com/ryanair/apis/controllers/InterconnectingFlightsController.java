package com.ryanair.apis.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ryanair.apis.resources.RyanairRouteResource;

@RestController
public class InterconnectingFlightsController {
	private static final String ROUTES_ENDPOINT = "https://api.ryanair.com/core/3/routes/";
	private static final String SCHEDULES_ENDPOINT = "https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}";

	@RequestMapping("/routes")
	public String routes(/*
							 * @RequestParam(value="name", defaultValue="World")
							 * String name
							 */) {
		// Only one time
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
		Unirest.setTimeouts(10000, 10000);
		// Response to Object
		HttpResponse<RyanairRouteResource[]> routesResponse = null;
		try {
			routesResponse = Unirest.get(ROUTES_ENDPOINT).asObject(RyanairRouteResource[].class);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RyanairRouteResource[] resources = routesResponse.getBody();

		StringBuilder builder = new StringBuilder();
		for (RyanairRouteResource resource : resources) {
			builder.append("Ryanair serves a flight from ").append(resource.getAirportFrom()).append(" to ")
					.append(resource.getAirportTo()).append("<br />");
		}

		// HttpResponse<Author> authorResponse =
		// Unirest.get("http://httpbin.org/books/{id}/author")
		// .routeParam("id", bookObject.getId())
		// .asObject(Author.class);
		//
		// Author authorObject = authorResponse.getBody();

		return builder.toString();
	}

}
