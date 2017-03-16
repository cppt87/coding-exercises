package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Status;

public class ClientErrorConflictException extends RestletApplicationException {

	private static final long serialVersionUID = 1480027661886792714L;

	public ClientErrorConflictException(String message) {
		super(MessageFormat.format(
				"raise 409 conflict: The request could not be completed due to a conflict with the current state of the resource. {0}", message
			));
		status = Status.CLIENT_ERROR_CONFLICT;
	}

}
