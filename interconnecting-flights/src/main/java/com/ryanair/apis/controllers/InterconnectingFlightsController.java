package com.ryanair.apis.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Deque;

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

import com.ryanair.apis.exceptions.BadRequestException;
import com.ryanair.apis.exceptions.NotFoundException;
import com.ryanair.apis.exceptions.RequestRangeNotSatisfiableException;
import com.ryanair.apis.models.SolutionResource;
import com.ryanair.apis.services.IInterconnectionsService;
import com.ryanair.apis.utils.InterconnectingFlightsUtils;

@RestController
@Validated
public class InterconnectingFlightsController {
	@Autowired
	private IInterconnectionsService<SolutionResource> interconnectionsService;

	@GetMapping(InterconnectingFlightsUtils.GET_INTERCONNECTIONS)
	public ResponseEntity<Deque<SolutionResource>> getInterconnections(
			@Pattern(regexp = InterconnectingFlightsUtils.IATA_REGEXP, message = InterconnectingFlightsUtils.IATA_DEP_ERROR_VALIDATION_MSG) @RequestParam(value = "departure", required = true) String departure,
			@Pattern(regexp = InterconnectingFlightsUtils.IATA_REGEXP, message = InterconnectingFlightsUtils.IATA_ARR_ERROR_VALIDATION_MSG) @RequestParam(value = "arrival", required = true) String arrival,
			@RequestParam(value = "departureDateTime", required = true) @Future(message = InterconnectingFlightsUtils.DATE_DEP_ERROR_VALIDATION_MSG) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date departureDateTime,
			@RequestParam(value = "arrivalDateTime", required = true) @Future(message = InterconnectingFlightsUtils.DATE_ARR_ERROR_VALIDATION_MSG) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date arrivalDateTime)
			throws Exception {

		// verify that arrivalDateTime > departureDateTime
		if (!arrivalDateTime.after(departureDateTime))
			throw new BadRequestException("Arrival date must be after the departure date");

		// verify that departure != arrival
		if (departure.equalsIgnoreCase(arrival))
			throw new BadRequestException("Departure IATA code and arrival IATA code must be different");

		// convert Date(s) to LocalDate(s)
		LocalDateTime departureLocalDateTime = InterconnectingFlightsUtils.dateToLocalDateTime(departureDateTime);
		LocalDateTime arrivalLocalDateTime = InterconnectingFlightsUtils.dateToLocalDateTime(arrivalDateTime);

		// verify that provided range is less than
		// InterconnectingFlightsUtils.MAX_DAYS_RANGE
		long days = departureLocalDateTime.until(arrivalLocalDateTime, ChronoUnit.DAYS);
		if (days > InterconnectingFlightsUtils.MAX_DAYS_RANGE)
			throw new RequestRangeNotSatisfiableException(String.format("%d days", days));
		// call service
		Deque<SolutionResource> solutions = this.interconnectionsService.getInterconnections(departure.toUpperCase(),
				arrival.toUpperCase(), departureLocalDateTime, arrivalLocalDateTime);
		if (solutions.isEmpty())
			throw new NotFoundException(String.format("No results found for the route [%s - %s]", departure, arrival));
		else
			return new ResponseEntity<Deque<SolutionResource>>(solutions, HttpStatus.OK);
	}
}
