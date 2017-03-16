package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Method;
import org.restlet.data.Status;

public class ClientErrorMethodNotAllowedException extends RestletApplicationException {

	private static final long serialVersionUID = 1480027661886792714L;

	public ClientErrorMethodNotAllowedException(Method actual, Method expected) {
		super(MessageFormat.format(
				"raise 405 method not allowed: [{0}] requested but [{1}] expected", actual.toString(), expected.toString()
			));
		status = Status.CLIENT_ERROR_METHOD_NOT_ALLOWED;
	}

	public ClientErrorMethodNotAllowedException(Method method) {
		super(MessageFormat.format(
				"raise 405 method not allowed: [{0}] requested but it is not defined for this api.", method.toString()
			));
		status = Status.CLIENT_ERROR_METHOD_NOT_ALLOWED;
	}

}
