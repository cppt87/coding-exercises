package com.ryanair.apis.exceptions;

import com.ryanair.apis.utils.InterconnectingFlightsUtils;

public class RequestRangeNotSatisfiableException extends Exception {

	/**
	 * 413 Exception
	 */
	private static final long serialVersionUID = 1L;

	public RequestRangeNotSatisfiableException(String range) {
		super(String.format(
				"Specified range [%s] is larger than the server is willing or able to process. Please try a smaller range [<= %d]",
				range, InterconnectingFlightsUtils.MAX_DAYS_RANGE));
	}

	public RequestRangeNotSatisfiableException(Throwable cause) {
		super(cause);
	}

	public RequestRangeNotSatisfiableException(String range, Throwable cause) {
		super(String.format(
				"Specified range [%s] is larger than the server is willing or able to process. Please try a smaller range [<= %d]",
				range, InterconnectingFlightsUtils.MAX_DAYS_RANGE), cause);
	}
}
