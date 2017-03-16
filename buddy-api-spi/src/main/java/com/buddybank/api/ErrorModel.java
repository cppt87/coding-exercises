package com.buddybank.api;

import com.buddybank.api.exceptions.RestletApplicationException;

public class ErrorModel {

	private int httpErrorCode;
	private String httpErrorDescription;
	private int errorCode;
	private String errorMessage;
	/**
	 * @param httpErrorCode
	 * @param httpErrorDescription
	 * @param errorCode
	 * @param errorMessage
	 */
	public ErrorModel(int httpErrorCode, String httpErrorDescription, int errorCode, String errorMessage) {
		super();
		this.httpErrorCode = httpErrorCode;
		this.httpErrorDescription = httpErrorDescription;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ErrorModel(RestletApplicationException e) {
		super();
		this.httpErrorCode = e.getStatus().getCode();
		this.httpErrorDescription = e.getStatus().getReasonPhrase();
		this.errorCode = -1;
		this.errorMessage = e.getMessage();		
	}
	
	public int getHttpErrorCode() {
		return httpErrorCode;
	}
	
	public String getHttpErrorDescription() {
		return httpErrorDescription;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
