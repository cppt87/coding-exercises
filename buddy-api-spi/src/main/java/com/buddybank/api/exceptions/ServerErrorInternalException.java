package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Status;

public class ServerErrorInternalException extends RestletApplicationException {

	private static final long serialVersionUID = -2939412045620755947L;

	public ServerErrorInternalException(Throwable t) {
		super(MessageFormat.format(
				"raise 500 internal server error: [{0}]", t.getMessage()
			), t);
		status = Status.SERVER_ERROR_INTERNAL;
	}

}
