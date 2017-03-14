package com.ryanair.apis.exceptions;

public class InternalServerErrorException extends Exception {

	/**
	 * Error 500 exception
	 */
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String message) {
		super(String.format("Server encountered an unexpected error which prevented it from fulfilling the request. %s",
				message));
	}

	public InternalServerErrorException(Throwable cause) {
		super(cause);
	}

	public InternalServerErrorException(String message, Throwable cause) {
		super(String.format("Server encountered an unexpected error which prevented it from fulfilling the request. %s",
				message), cause);
	}
}
