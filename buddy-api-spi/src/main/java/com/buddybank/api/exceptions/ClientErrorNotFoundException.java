package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Status;

public class ClientErrorNotFoundException extends RestletApplicationException {

	private static final long serialVersionUID = -5665783009988679196L;

	public ClientErrorNotFoundException(String paramName, String paramValue) {
		super(MessageFormat.format(
				"raise 404 not found: the entity having [{0}]=[{1}]", paramName, paramValue
			));
		status = Status.CLIENT_ERROR_NOT_FOUND;
	}

	public ClientErrorNotFoundException(String paramName, String paramValue, String subParamName, String subParamValue) {
		super(MessageFormat.format(
				"raise 404 not found: the entity having [{0}]=[{1}],[{2}]=[{3}]", paramName, paramValue, subParamName, subParamValue
			));
		status = Status.CLIENT_ERROR_NOT_FOUND;
	}

}
