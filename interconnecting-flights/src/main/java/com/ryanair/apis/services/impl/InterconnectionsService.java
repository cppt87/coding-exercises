package com.ryanair.apis.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.ryanair.apis.external.models.Day;
import com.ryanair.apis.external.models.Flight;
import com.ryanair.apis.external.models.RyanairRouteResource;
import com.ryanair.apis.external.models.RyanairScheduleResource;
import com.ryanair.apis.external.services.IRyanairAPIsService;
import com.ryanair.apis.models.Leg;
import com.ryanair.apis.models.SolutionResource;
import com.ryanair.apis.services.IInterconnectionsService;
import com.ryanair.apis.utils.DirectFlightEnum;
import com.ryanair.apis.utils.InterconnectingFlightsUtils;

@RequestScope
@Service("interconnectionsService")
public class InterconnectionsService implements IInterconnectionsService<SolutionResource> {
	// constants definition
	private static final int DEPARTURE = 0;
	private static final int STOP = 1;
	private static final int ARRIVAL = 2;
	@Autowired
	// Ryanair APIs
	private IRyanairAPIsService ryanairService;
	@Autowired
	// Cache of RyanairScheduleResource instances
	private Map<String, RyanairScheduleResource> cache;
	@Autowired
	// retrieve the routes list from Ryanair's Routes API service
	private RyanairRouteResource[] routes;
	// LIFO queue of solutions
	private Deque<SolutionResource> solutions;

	public InterconnectionsService() {
		this.solutions = new ArrayDeque<SolutionResource>();
	}

	@Override
	public Deque<SolutionResource> getInterconnections(String departure, String arrival, LocalDateTime departureTime,
			LocalDateTime arrivalTime) throws Exception {
		/*
		 * list of 0 and/or 1 stop routes. Later we will iterate over a useful
		 * list of this type: [["DUB", "WRO"], ["DUB", "STN", "WRO"]]. List is
		 * ordered; if a direct flight exists, it will be the last element.
		 */
		List<List<String>> foundRoutes = new ArrayList<List<String>>();
		/*
		 * In order to extract all the 1 stop flights, we need to create such an
		 * adjacency list: [key=STOP -> value=FROM || key=STOP -> value=TO].
		 */
		Properties graph = new Properties();

		// for each route coming from Ryanair's Routes API service
		for (RyanairRouteResource route : routes) {
			// case of direct flight: same departure and arrival
			if (departure.equalsIgnoreCase(route.getAirportFrom()) && arrival.equalsIgnoreCase(route.getAirportTo())) {
				this.createAndAddRoute(departure, arrival, foundRoutes);
				// case of 1 stop flight: same departure and different arrival
			} else if (departure.equalsIgnoreCase(route.getAirportFrom())
					&& !arrival.equalsIgnoreCase(route.getAirportTo())) {
				/*
				 * if the adjacency list already contains the key=STOP
				 * (different arrival), an insert of type key=STOP -> value=TO
				 * already took place previously
				 */
				if (graph.containsKey(route.getAirportTo()))
					this.createAndAddRoute(departure, route.getAirportTo(), arrival, foundRoutes);
				/*
				 * otherwise, perform the insert key=STOP -> value=FROM
				 */
				else
					graph.put(route.getAirportTo(), departure);
				// case of 1 stop flight: same arrival and different departure
			} else if (!departure.equalsIgnoreCase(route.getAirportFrom())
					&& arrival.equalsIgnoreCase(route.getAirportTo())) {
				/*
				 * if the adjacency list already contains the key=STOP
				 * (different arrival), an insert of type key=STOP -> value=FROM
				 * already took place previously
				 */
				if (graph.containsKey(route.getAirportFrom()))
					this.createAndAddRoute(departure, route.getAirportFrom(), arrival, foundRoutes);
				/*
				 * otherwise, perform the insert key=STOP -> value=TO
				 */
				else
					graph.put(route.getAirportFrom(), arrival);
			}
		}
		// sort found routes by number of stops, in a descending way
		Collections.sort(foundRoutes, new Comparator<List<String>>() {

			@Override
			public int compare(List<String> o1, List<String> o2) {
				return o2.size() - o1.size();
			}
		});

		// for each route
		for (List<String> foundRoute : foundRoutes) {
			// calculate solutions
			this.combineSolutions(foundRoute, departureTime, arrivalTime);
		}

		return this.solutions;
	}

	/**
	 * Creates the list of 0 and/or 1 stop solutions, managing a certain route.
	 * In case of 1 stop solution (a list of three IATA codes), it behaves
	 * recursively: for each suitable {@link Leg} based on first two IATA codes,
	 * it applies itself over the second an the third element of the list
	 * (namely the second {@link Leg})
	 * 
	 * @param route
	 *            list of IATA codes. Two elements if direct, three if not
	 * @param departureTime
	 *            the departure input time
	 * @param arrivalTime
	 *            the arrival input time
	 */
	private void combineSolutions(List<String> route, LocalDateTime departureTime, LocalDateTime arrivalTime)
			throws Exception {
		// cycle over different months
		for (int month = departureTime.getMonthValue(); month <= arrivalTime.getMonthValue(); month++) {
			/*
			 * create a unique cache key based on concatenation of departure
			 * IATA code, arrival IATA code, departure year and departure month
			 */
			String key = new StringBuilder(route.get(DEPARTURE)).append(route.get(STOP)).append(departureTime.getYear())
					.append(month).toString();
			// retrieve schedules from cache (if exists)...
			RyanairScheduleResource schedules;
			if (this.cache.containsKey(key)) {
				schedules = this.cache.get(key);
				// ... or from Ryanair's Schedules API service
			} else {
				schedules = this.ryanairService.schedulesAPI(route.get(DEPARTURE), route.get(STOP),
						departureTime.getYear(), month);
				this.cache.put(key, schedules);
			}

			/*
			 * ... until provided arrival time is not after the undergoing day
			 * (useful to save iterations due to the ordered list returned by
			 * Ryanair's Schedules API service)
			 */
			for (Day day : schedules.getDays()) {
				// build flight departure date
				LocalDateTime flightDeparture = departureTime.withMonth(month).withDayOfMonth(day.getDay());
				// if we are between departure and arrival day (inclusive)
				if (!flightDeparture.isBefore(departureTime) && !flightDeparture.isAfter(arrivalTime)) {
					// check each flight of the day
					for (Flight flight : day.getFlights()) {
						// get flight departure date-time
						flightDeparture = flightDeparture.with(LocalTime.parse(flight.getDepartureTime()));
						// get flight arrival date-time
						LocalDateTime flightArrival = LocalDateTime.of(
								LocalDate.of(flightDeparture.getYear(), month, day.getDay()),
								LocalTime.parse(flight.getArrivalTime()));
						// check data interval (inclusive)
						if (!flightDeparture.isBefore(departureTime) && !flightArrival.isAfter(arrivalTime)) {
							// it is a suitable leg; let's create the resource
							Leg leg = new Leg();
							leg.setDepartureAirport(route.get(DEPARTURE));
							leg.setArrivalAirport(route.get(STOP));
							leg.setDepartureDateTime(InterconnectingFlightsUtils.localDateTimeToDate(flightDeparture));
							leg.setArrivalDateTime(InterconnectingFlightsUtils.localDateTimeToDate(flightArrival));
							// case of non direct route
							if (route.size() == 3) {
								/*
								 * create a partial solution containing the
								 * first of two needed legs
								 */
								this.createAndPushSolution(DirectFlightEnum.NON_DIRECT_FLIGHT, leg);
								/*
								 * recursive call over the next part of the
								 * route. In order to meet the requirements, we
								 * provide a departure time limit increased of
								 * two more hours towards to the first leg's
								 * arrival one
								 */
								this.combineSolutions(route.subList(STOP, ARRIVAL + 1), flightArrival.plusHours(2),
										arrivalTime);
								/*
								 * remove last incomplete solution, if exists
								 * (coming from next case)
								 */
								if (this.solutions.peek().getStops() == 1
										&& this.solutions.peek().getLegs().size() == 1) {
									this.solutions.pop();
								}
								/*
								 * case of second leg analysis (case managed
								 * only during a recursive call). Is the last
								 * created solution inside the the queue
								 * partial? It means we must complete it
								 * creating the second leg
								 */
							} else if (route.size() == 2 && !this.solutions.isEmpty()
									&& this.solutions.peek().getStops() == 1
									&& this.solutions.peek().getLegs().size() == 1) {
								/*
								 * get a copy of the first leg, extracted from
								 * the last created solution
								 */
								Leg first = this.solutions.peek().getLegs().iterator().next();
								// complete the last created solution
								this.solutions.peek().getLegs().add(leg);
								/*
								 * if there are more flights to check, create a
								 * new instance of (partial) Solution, making
								 * use of the just copied first leg
								 */
								this.createAndPushSolution(DirectFlightEnum.NON_DIRECT_FLIGHT, first);
								/*
								 * case of direct flight. Create a single leg
								 * Solution and push it into the solutions queue
								 */
							} else {
								this.createAndPushSolution(DirectFlightEnum.DIRECT_FLIGHT, leg);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates a {@link SolutionResource} based on number of stops (0 or 1) and
	 * provided {@link Leg}
	 * 
	 * @param direct
	 *            whether is a direct or non direct flight
	 * @param firstLeg
	 *            first (and last if direct) leg
	 */
	private void createAndPushSolution(DirectFlightEnum direct, Leg firstLeg) {
		SolutionResource solution = new SolutionResource();
		// direct or non direct flight?
		solution.setStops(direct.ordinal());
		Set<Leg> legs = new LinkedHashSet<Leg>(direct.ordinal() + 1);
		// in any case, add the first leg
		legs.add(firstLeg);
		solution.setLegs(legs);
		// push the solution into the queue
		this.solutions.push(solution);
	}

	/**
	 * Creates a couple of IATA codes representing a direct route, and adds it
	 * to the final list of suitable routes
	 * 
	 * @param departure
	 *            departure IATA code
	 * @param arrival
	 *            arrival IATA code
	 * @param toRoutesList
	 *            list for adding just created route
	 * @return true (as specified by {@link Collection.add})
	 */
	private boolean createAndAddRoute(String departure, String arrival, List<List<String>> toRoutesList) {
		// create a two elements long list
		List<String> zeroStopRoute = new ArrayList<String>(2);
		// add departure IATA code
		zeroStopRoute.add(DEPARTURE, departure);
		// add arrival IATA code
		zeroStopRoute.add(STOP, arrival);
		// add the list to the list of suitable ones
		return toRoutesList.add(zeroStopRoute);
	}

	/**
	 * Creates a triple of IATA codes representing a one-stop route, and adds it
	 * to the final list of suitable routes
	 * 
	 * @param departure
	 *            departure IATA code
	 * @param stop
	 *            stop IATA code
	 * @param arrival
	 *            arrival IATA code
	 * @param toRoutesList
	 *            list for adding just created route
	 * @return true (as specified by {@link Collection.add})
	 */
	private boolean createAndAddRoute(String departure, String stop, String arrival, List<List<String>> toRoutesList) {
		// create a three elements long list
		List<String> oneStopRoute = new ArrayList<String>(3);
		// add departure IATA code
		oneStopRoute.add(DEPARTURE, departure);
		// add stop IATA code
		oneStopRoute.add(STOP, stop);
		// add arrival IATA code
		oneStopRoute.add(ARRIVAL, arrival);
		// add the list to the list of suitable ones
		return toRoutesList.add(oneStopRoute);
	}
}