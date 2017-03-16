package com.buddybank.api;

import org.restlet.representation.Representation;

import com.buddybank.api.exceptions.RestletApplicationException;

public interface ErrorReporter {

	public abstract Representation emitError(RestletApplicationException e);

}