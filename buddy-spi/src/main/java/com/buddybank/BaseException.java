package com.buddybank;


public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -3051374168769396331L;

	public BaseException(String message, Throwable t) {
		super(message, t);
	}

}
