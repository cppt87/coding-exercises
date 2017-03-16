package com.buddybank.api.exceptions;

import org.restlet.data.Status;

import com.buddybank.api.logger.BBApiLogger;
import com.buddybank.api.logger.HttpRequestId;
import com.google.common.base.Throwables;

public class RestletApplicationException extends Exception {

	private static final long serialVersionUID = 6564926750416797926L;
	
	protected static final BBApiLogger APILOG = new BBApiLogger(RestletApplicationException.class);

	protected Status status;
	
	protected /* final */ HttpRequestId requestId;

	protected boolean logStacktrace = true;

	public RestletApplicationException(String message) {
		super(message);
		logException();
	}

	public RestletApplicationException(String message, Throwable t) {
		super(message, t);
		logException();
	}

	public RestletApplicationException() {
		super();
		logException();
	}

	public Status getStatus() {
		return status;
	}
	
	protected void logException() {
    	APILOG.log();
    	APILOG.append("exception");
    	if (requestId != null) {
    		APILOG.append(" requestid", requestId);
    	}
    	if (status != null) {
    		APILOG.append(" status", status.getCode());
    	}
    	APILOG.append(" message", this.getMessage());
    	if (logStacktrace ) {
    		APILOG.append("\n-------\n");
    		APILOG.append(" stacktrace", Throwables.getStackTraceAsString(this));
    		APILOG.append("\n-------\n");
    	}
    	APILOG.append(" method", "logException");
    	APILOG.error();
	}
	
}
