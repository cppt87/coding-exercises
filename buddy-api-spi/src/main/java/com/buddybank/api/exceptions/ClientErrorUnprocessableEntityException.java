package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.Status;

public class ClientErrorUnprocessableEntityException extends RestletApplicationException {

	private static final long serialVersionUID = 3142318790586594726L;

	public ClientErrorUnprocessableEntityException(String param) {
		super(MessageFormat.format(
				"raise 422 unprocessable entity: parameter [{0}] expected", param.toString()
			));
		status = Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY;
	}

	public ClientErrorUnprocessableEntityException(String name, String dataType, String value) {
		super(MessageFormat.format(
				"raise 422 unprocessable entity: the value [{0}] for parameter [{1}] is not parsable to: {2}.", value, name, dataType
			));
		status = Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY;
	}

	public ClientErrorUnprocessableEntityException(String name, String dataType, Object v) {
		super(MessageFormat.format(
				"raise 422 unprocessable entity: the value [{0}] for parameter [{1}] is not parsable to: {2}.", (v != null ? v.toString() : ""), name, dataType
			));
		status = Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY;
	}

}
