package com.ryanair.apis.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class InterconnectingFlightsUtils {

	/*
	 * REST URIs definition
	 */
	public static final String GET_INTERCONNECTIONS = "/interconnections";

	/*
	 * Global constants definition
	 */
	// All three characters long words (ignoring case)
	public static final String IATA_REGEXP = "^[a-zA-Z]{3}$";
	public static final String IATA_DEP_ERROR_VALIDATION_MSG = "Provided departure [%s] is not a valid IATA airport code. It must be 3 characters long";
	public static final String IATA_ARR_ERROR_VALIDATION_MSG = "Provided arrival [%s] is not a valid IATA airport code. It must be 3 characters long";
	public static final String DATE_DEP_ERROR_VALIDATION_MSG = "Departure date must be in the future";
	public static final String DATE_ARR_ERROR_VALIDATION_MSG = "Arrival date must be in the future";
	public static final int MAX_DAYS_RANGE = 5;

	/*
	 * Utility methods definition
	 */
	/**
	 * Converts a {@link Date} into {@link LocalDateTime}
	 * 
	 * @param date
	 *            {@link Date} object to be converted, not null
	 * 
	 * @return {@link LocalDateTime} version
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * Converts a {@link LocalDateTime} into {@link Date}
	 * 
	 * @param localDateTime
	 *            {@link LocalDateTime} object to be converted, not null
	 * 
	 * @return {@link Date} version
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Creates a {@link LocalDateTime} object representing a flight time
	 * schedule
	 * 
	 * @param year
	 *            the year to represent, from MIN_YEAR to MAX_YEAR, not null
	 * @param monthValue
	 *            the month-of-year to represent, from 1 (January) to 12
	 *            (December), not null
	 * @param dayMonth
	 *            the day-of-month to represent, from 1 to 31, not null
	 * @param time
	 *            the text-based time to parse such as "10:15:30", not null
	 * @return the assembled {@link LocalDateTime} object
	 */
	public static LocalDateTime buildFlightTime(int year, int monthValue, int dayMonth, String time) {
		return LocalDateTime.of(LocalDate.of(year, monthValue, dayMonth), LocalTime.parse(time));
	}
}
