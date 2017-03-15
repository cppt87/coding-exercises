package com.ryanair.apis.exceptions;

public class NotFoundException extends Exception {

	/**
	 * 404 Not found exception
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super("Resource not found");
	}
	
	public NotFoundException(String msg) {
		super(msg);
	}
	
	public NotFoundException(Throwable cause) {
		super(cause);
	}

}
