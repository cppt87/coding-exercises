package com.buddybank.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Methods for dealing with timestamps
 */
public class TimestampUtils {
	
	public static final long SECONDS_IN_YEAR = (365 * 24 * 60 * 60);

	/**
	 * Return an ISO 8601 combined date and time string for current date/time
	 * 
	 * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
	 */
	public static String getISO8601StringForCurrentDate() {
		Date now = new Date();
		return getISO8601StringForDate(now);
	}

	/**
	 * Return an ISO 8601 combined date and time string for specified date/time
	 * 
	 * @param date
	 *            Date
	 * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
	 */
	public static String getISO8601StringForDate(Date date) {
		if (date == null) {
			throw new NullPointerException("getISO8601StringForDate: date must be set");
		}
		return formatter().print(date.getTime());
	}

	public static String getShortStringForDate(Date date) {
		if (date == null) {
			throw new NullPointerException("getISO8601StringForDate: date must be set");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
		return formatter.format(date);
	}
	
	/**
	 * Return a java.util.Date object for an ISO 8601 time string
	 * 
	 * @param ts
	 *        String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
	 * @throws ParseException 
	 */
	public static Date getDateForISO8601String(String ts) throws ParseException {
		return formatter().parseDateTime(ts).toDate();
	}

	public static Date getDateForString(String ts, String... allowedFormats) throws ParseException{
		DateFormat formatter = null;
		ParseException lastException = new ParseException("", 0);
		for (String f : allowedFormats) {
			formatter = new SimpleDateFormat(f, Locale.ITALY);
			Date d;
			try {
				d = formatter.parse(ts);
			} catch (ParseException e) {
				lastException = e;
				continue;
			}
			return d;
		}
		throw lastException;
	}

	private static DateTimeFormatter formatter() {
		DateTimeFormatter dateFormat = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC().withLocale(Locale.ITALY);
		return dateFormat;		
	}

	/**
	 * Private constructor: class cannot be instantiated
	 */
	private TimestampUtils() {
	}
}