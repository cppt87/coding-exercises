package com.ryanair.apis.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.apis.InterconnectingFlightsRestURIConstants;
import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.services.IRyanairAPIsService;

@RestController
@Validated
public class InterconnectingFlightsController {
	private static final int DEPARTURE = 0;
	private static final int STOP = 1;
	private static final int ARRIVAL = 2;

	@Autowired
	IRyanairAPIsService ryanairService;

	@GetMapping(InterconnectingFlightsRestURIConstants.GET_INTERCONNECTIONS)
	public ResponseEntity<List<String[]>> routes(
			@Pattern(regexp = "^[a-zA-Z]{3}$", message = "Provided departure [%s] is not a valid IATA airport code. It must be 3 characters long") @RequestParam(value = "departure", required = true) String departure,
			@Pattern(regexp = "^[a-zA-Z]{3}$", message = "Provided arrival [%s] is not a valid IATA airport code. It must be 3 characters long") @RequestParam(value = "arrival", required = true) String arrival,
			@RequestParam(value = "departureDateTime", required = true) @Future(message = "Departure date must be in the future") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date departureDateTime,
			@RequestParam(value = "arrivalDateTime", required = true) @Future(message = "Arrival date must be in the future") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date arrivalDateTime) {

		RyanairRouteResource[] routes = this.ryanairService.routesAPI();

		List<String[]> foundRoutes = new ArrayList<String[]>();
		Properties graph = new Properties();

		for (RyanairRouteResource route : routes) {
			if (departure.equalsIgnoreCase(route.getAirportFrom()) && arrival.equalsIgnoreCase(route.getAirportTo())) {
				String[] zeroStopRoute = new String[2];
				zeroStopRoute[DEPARTURE] = departure;
				zeroStopRoute[STOP] = arrival;
				foundRoutes.add(zeroStopRoute);
			} else if (departure.equalsIgnoreCase(route.getAirportFrom())
					&& !arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (graph.containsKey(route.getAirportTo())) {
					String[] oneStopRoute = new String[3];
					oneStopRoute[DEPARTURE] = departure;
					oneStopRoute[STOP] = route.getAirportTo();
					oneStopRoute[ARRIVAL] = arrival;
					foundRoutes.add(oneStopRoute);
				} else {
					graph.put(route.getAirportTo(), departure);
				}
			} else if (!departure.equalsIgnoreCase(route.getAirportFrom())
					&& arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (graph.containsKey(route.getAirportFrom())) {
					String[] oneStopRoute = new String[3];
					oneStopRoute[DEPARTURE] = departure;
					oneStopRoute[STOP] = route.getAirportFrom();
					oneStopRoute[ARRIVAL] = arrival;
					foundRoutes.add(oneStopRoute);
				} else {
					graph.put(route.getAirportFrom(), arrival);
				}
			}
		}

		// date conversion: 2016-03-01T07:00

		// for (String[] foundRoute : foundRoutes) {
		// // RyanairScheduleResource[] schedules =
		// // this.ryanairService.schedulesAPI(foundRoute[DEPARTURE],
		// // foundRoute[STOP], year, month);
		// }

		return foundRoutes.isEmpty() ? new ResponseEntity<List<String[]>>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<List<String[]>>(foundRoutes, HttpStatus.OK);
	}
}
