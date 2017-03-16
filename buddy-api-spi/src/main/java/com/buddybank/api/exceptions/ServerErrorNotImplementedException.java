package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Method;
import org.restlet.data.Status;

public class ServerErrorNotImplementedException extends RestletApplicationException {

	private static final long serialVersionUID = -5803058191598802395L;

	public ServerErrorNotImplementedException(Method method, String message) {
		super(MessageFormat.format(
				"raise 501 not implemented server error: Http Method [{0}] {1}", method.toString(), message
			));
		status = Status.SERVER_ERROR_NOT_IMPLEMENTED;
	}

}
