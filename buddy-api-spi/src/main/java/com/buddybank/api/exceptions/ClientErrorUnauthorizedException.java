package com.buddybank.api.exceptions;

import org.restlet.data.Status;

public class ClientErrorUnauthorizedException extends RestletApplicationException {

	private static final long serialVersionUID = 3478906806749809343L;

	public ClientErrorUnauthorizedException(Throwable t) {
		super(
			"raise 403 bad request"
			, t);
		status = Status.CLIENT_ERROR_UNAUTHORIZED;
	}

	public ClientErrorUnauthorizedException() {
		super();
		status = Status.CLIENT_ERROR_UNAUTHORIZED;
	}

}
