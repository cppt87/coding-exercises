package com.ryanair.apis.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.apis.InterconnectingFlightsRestURIConstants;
import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.services.IRyanairAPIsService;

@RestController
public class InterconnectingFlightsController {
	@Autowired
	IRyanairAPIsService ryanairService;

	@GetMapping(InterconnectingFlightsRestURIConstants.GET_INTERCONNECTIONS)
	public ResponseEntity<List<String[]>> routes(
			@Size(max = 3, min = 3, message = "departure must be 3 characters long") @RequestParam(value = "departure", required = true) String departure,
			@Size(max = 3, min = 3, message = "arrival must be 3 characters long") @RequestParam(value = "arrival", required = true) String arrival,
			@RequestParam(value = "departureDateTime", required = true) String departureDateTime,
			@RequestParam(value = "arrivalDateTime", required = true) String arrivalDateTime) {

		RyanairRouteResource[] routes = this.ryanairService.routesAPI();

		List<String[]> foundRoutes = new ArrayList<String[]>();
		Properties matrix = new Properties();

		for (RyanairRouteResource route : routes) {
			if (departure.equalsIgnoreCase(route.getAirportFrom()) && arrival.equalsIgnoreCase(route.getAirportTo())) {
				String[] zeroStopRoute = new String[2];
				zeroStopRoute[0] = departure;
				zeroStopRoute[1] = arrival;
				foundRoutes.add(zeroStopRoute);
			} else if (departure.equalsIgnoreCase(route.getAirportFrom())
					&& !arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (matrix.containsKey(route.getAirportTo())) {
					String[] oneStopRoute = new String[3];
					oneStopRoute[0] = departure;
					oneStopRoute[1] = route.getAirportTo();
					oneStopRoute[2] = arrival;
					foundRoutes.add(oneStopRoute);
				} else {
					matrix.put(route.getAirportTo(), departure);
				}
			} else if (!departure.equalsIgnoreCase(route.getAirportFrom())
					&& arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (matrix.containsKey(route.getAirportFrom())) {
					String[] oneStopRoute = new String[3];
					oneStopRoute[0] = departure;
					oneStopRoute[1] = route.getAirportFrom();
					oneStopRoute[2] = arrival;
					foundRoutes.add(oneStopRoute);
				} else {
					matrix.put(route.getAirportFrom(), arrival);
				}
			}
		}

		return foundRoutes.isEmpty() ? new ResponseEntity<List<String[]>>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<List<String[]>>(foundRoutes, HttpStatus.OK);
	}
}
