package com.ryanair.apis.exceptions;

public class BadRequestException extends Exception {
	/**
	 * Error 400 exception
	 */
	private static final long serialVersionUID = 1L;
	
	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
