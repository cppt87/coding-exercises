package com.buddybank.api.exceptions;

import java.text.MessageFormat;

import org.restlet.data.MediaType;
import org.restlet.data.Status;

public class ClientErrorNotAcceptableException extends RestletApplicationException {

	private static final long serialVersionUID = -3620870313460277525L;

	public ClientErrorNotAcceptableException(MediaType actual, MediaType expected) {
		super(MessageFormat.format(
			"raise 406 not acceptable: actual media type [{0}], expected [{1}]", actual.toString(), expected.toString()
		));
		status = Status.CLIENT_ERROR_NOT_ACCEPTABLE;
	}

}
