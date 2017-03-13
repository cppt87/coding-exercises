package com.ryanair.apis.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

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
import com.ryanair.apis.models.Day;
import com.ryanair.apis.models.Flight;
import com.ryanair.apis.models.Leg;
import com.ryanair.apis.models.RyanairRouteResource;
import com.ryanair.apis.models.RyanairScheduleResource;
import com.ryanair.apis.models.SolutionResource;
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
	public ResponseEntity<Stack<SolutionResource>> routes(
			@Pattern(regexp = "^[a-zA-Z]{3}$", message = "Provided departure [%s] is not a valid IATA airport code. It must be 3 characters long") @RequestParam(value = "departure", required = true) String departure,
			@Pattern(regexp = "^[a-zA-Z]{3}$", message = "Provided arrival [%s] is not a valid IATA airport code. It must be 3 characters long") @RequestParam(value = "arrival", required = true) String arrival,
			@RequestParam(value = "departureDateTime", required = true) @Future(message = "Departure date must be in the future") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date departureDateTime,
			@RequestParam(value = "arrivalDateTime", required = true) @Future(message = "Arrival date must be in the future") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date arrivalDateTime) {

		RyanairRouteResource[] routes = this.ryanairService.routesAPI();

		List<List<String>> foundRoutes = new ArrayList<List<String>>();
		Properties graph = new Properties();

		for (RyanairRouteResource route : routes) {
			if (departure.equalsIgnoreCase(route.getAirportFrom()) && arrival.equalsIgnoreCase(route.getAirportTo())) {
				List<String> zeroStopRoute = new ArrayList<String>(2);
				zeroStopRoute.add(DEPARTURE, departure);
				zeroStopRoute.add(STOP, arrival);
				foundRoutes.add(zeroStopRoute);
			} else if (departure.equalsIgnoreCase(route.getAirportFrom())
					&& !arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (graph.containsKey(route.getAirportTo())) {
					List<String> oneStopRoute = new ArrayList<String>(3);
					oneStopRoute.add(DEPARTURE, departure);
					oneStopRoute.add(STOP, route.getAirportTo());
					oneStopRoute.add(ARRIVAL, arrival);
					foundRoutes.add(oneStopRoute);
				} else {
					graph.put(route.getAirportTo(), departure);
				}
			} else if (!departure.equalsIgnoreCase(route.getAirportFrom())
					&& arrival.equalsIgnoreCase(route.getAirportTo())) {
				if (graph.containsKey(route.getAirportFrom())) {
					List<String> oneStopRoute = new ArrayList<String>(3);
					oneStopRoute.add(DEPARTURE, departure);
					oneStopRoute.add(STOP, route.getAirportFrom());
					oneStopRoute.add(ARRIVAL, arrival);
					foundRoutes.add(oneStopRoute);
				} else {
					graph.put(route.getAirportFrom(), arrival);
				}
			}
		}

		// convert Date(s) to LocalDate(s)
		LocalDateTime departureLocalDateTime = departureDateTime.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		LocalDateTime arrivalLocalDateTime = arrivalDateTime.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		// create a cache where store checked schedules
		Map<String, RyanairScheduleResource> cache = new HashMap<String, RyanairScheduleResource>();
		// create list of solutions to be returned
		Stack<SolutionResource> solutions = new Stack<SolutionResource>();
		// for each route
		for (List<String> foundRoute : foundRoutes) {
			// GOOD LUCK!
			this.combineSolutions(foundRoute, departureLocalDateTime, arrivalLocalDateTime, cache, solutions);
		}

		return solutions.isEmpty() ? new ResponseEntity<Stack<SolutionResource>>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<Stack<SolutionResource>>(solutions, HttpStatus.OK);

	}

	private void combineSolutions(List<String> route, LocalDateTime departureTime, LocalDateTime arrivalTime,
			Map<String, RyanairScheduleResource> cache, Stack<SolutionResource> solutions) {
		// create list of year
		while (departureTime.isBefore(arrivalTime)) {
			StringBuilder builder = new StringBuilder(route.get(DEPARTURE)).append(route.get(STOP))
					.append(departureTime.getYear()).append(departureTime.getMonthValue());
			String key = builder.toString();
			RyanairScheduleResource schedules;
			if (cache.containsKey(key)) {
				schedules = cache.get(key);
			} else {
				schedules = this.ryanairService.schedulesAPI(route.get(DEPARTURE), route.get(STOP),
						departureTime.getYear(), departureTime.getMonthValue());
				cache.put(key, schedules);
			}

			// check over suitable flights
			Iterator<Day> it = schedules.getDays().iterator();
			Day day;
			while (it.hasNext() && (day = it.next()).getDay() <= arrivalTime.getDayOfMonth()) {
				if (day.getDay() >= departureTime.getDayOfMonth()) {
					// check flights
					for (Flight flight : day.getFlights()) {
						// get flight departure date-time
						LocalDateTime flightDeparture = LocalDateTime.of(
								LocalDate.of(departureTime.getYear(), departureTime.getMonthValue(), day.getDay()),
								LocalTime.parse(flight.getDepartureTime()));
						// get flight arrival date-time
						LocalDateTime flightArrival = LocalDateTime.of(
								LocalDate.of(departureTime.getYear(), departureTime.getMonthValue(), day.getDay()),
								LocalTime.parse(flight.getArrivalTime()));
						if (flightDeparture.isAfter(departureTime) && flightArrival.isBefore(arrivalTime)) {
							Leg leg = new Leg();
							leg.setDepartureAirport(route.get(DEPARTURE));
							leg.setArrivalAirport(route.get(STOP));
							leg.setDepartureDateTime(
									Date.from(flightDeparture.atZone(ZoneId.systemDefault()).toInstant()));
							leg.setArrivalDateTime(Date.from(flightArrival.atZone(ZoneId.systemDefault()).toInstant()));
							if (route.size() == 3) {
								solutions.push(this.createSolution(1, leg));
								// recursive call
								this.combineSolutions(route.subList(STOP, ARRIVAL + 1), flightArrival.plusHours(2),
										arrivalTime, cache, solutions);
								// remove last incomplete solution, if exists
								if (solutions.peek().getStops() == 1 && solutions.peek().getLegs().size() == 1) {
									solutions.pop();
								}
							} else if (route.size() == 2 && !solutions.isEmpty() && solutions.peek().getStops() == 1
									&& solutions.peek().getLegs().size() == 1) {
								Leg first = solutions.peek().getLegs().iterator().next();
								solutions.peek().getLegs().add(leg);
								// create a new instance of Solution
								solutions.push(this.createSolution(1, first));
							} else {
								solutions.push(this.createSolution(0, leg));
							}
						}
					}
				}
			}

			// add one month
			departureTime = departureTime.plusMonths(1);
		}
	}

	private SolutionResource createSolution(int stops, Leg leg) {
		SolutionResource solution = new SolutionResource();
		solution.setStops(stops);
		Set<Leg> legs = new LinkedHashSet<Leg>(stops + 1);
		legs.add(leg);
		solution.setLegs(legs);
		return solution;
	}
}
