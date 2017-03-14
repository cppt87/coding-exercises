package com.ryanair.apis.exceptions;

public class ServiceUnavailableException extends Exception {
	/**
	 * Error 503 exception
	 */
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(String service) {
		super(String.format("Service [%s] is currently unavailable", service));
	}

	public ServiceUnavailableException(Throwable cause) {
		super(cause);
	}

	public ServiceUnavailableException(String service, Throwable cause) {
		super(String.format("Service [%s] is currently unavailable", service), cause);
	}
}
