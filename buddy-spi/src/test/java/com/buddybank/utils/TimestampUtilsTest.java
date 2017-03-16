package com.buddybank.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.Date;

import org.testng.annotations.Test;

import com.buddybank.utils.TimestampUtils;

public class TimestampUtilsTest {

	@Test
	public void testDateToISOString() throws Exception {
		String ds = TimestampUtils.getISO8601StringForDate(new Date(1454151823658L));
		assertThat(ds).as("datestring").isEqualTo("2016-01-30T11:03:43Z");
	}

	@Test
	public void testDateToShortString() throws Exception {
		String ds = TimestampUtils.getShortStringForDate(new Date(1454151823658L));
		assertThat(ds).as("datestring").isEqualTo("2016-01-30");
	}

	@Test
	public void testStringToDateWithAllowedFormats() throws Exception {
		Date datetime = TimestampUtils.getDateForString("2016-01-30", "yyyy-MM-dd");
		assertThat(datetime).as("datetime").hasDayOfMonth(30).hasMonth(1).hasYear(2016);
	}

	@Test(expectedExceptions = ParseException.class)
	public void testStringToDateWithAllowedFormatsThrowsExceptionIfNoFormat() throws Exception {
		TimestampUtils.getDateForString("2016-01-30", new String[]{});
	}

	@Test(expectedExceptions = ParseException.class)
	public void testStringToDateWithAllowedFormatsThrowsExceptionIfWrongFormat() throws Exception {
		TimestampUtils.getDateForString("30/01/2016", "yyyy-MM-dd");
	}
	
	@Test
	public void testFromAndToDate() throws Exception {
		long allowedDeltaInMillis = 999L;
		Date date = new Date(1454151823658L);
		String datestring = TimestampUtils.getISO8601StringForDate(date);
		Date datetime = TimestampUtils.getDateForISO8601String(datestring);
		assertThat(datetime).as("datetime").isCloseTo(date, allowedDeltaInMillis);
	}

}
