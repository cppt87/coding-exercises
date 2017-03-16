package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Status;

public class ServerErrorServiceUnavailableException extends RestletApplicationException {

	private static final long serialVersionUID = -5803058191598802395L;

	public ServerErrorServiceUnavailableException(String message) {
		super(MessageFormat.format(
				"raise 503 service unavailable server error: service [{0}] is temporarily unavailable", message));
		status = Status.SERVER_ERROR_SERVICE_UNAVAILABLE;
	}

	public ServerErrorServiceUnavailableException(String service, String code, String message) {
		super(MessageFormat.format(new StringBuilder(
				"raise 503 service unavailable server error: service [{0}] is temporarily unavailable due to following cause: \n")
						.append("- [{0}] error code: [{1}]\n")
						.append("- [{0}] error description: [{2}]\n")
						.toString(),
				service, code, message));
		status = Status.SERVER_ERROR_SERVICE_UNAVAILABLE;
	}
}
