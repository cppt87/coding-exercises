package com.buddybank.api.dataproviders;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPEntityAction<T> extends IDPEntityReader<T> {
	
	public T doAction(T entity, String actionId, RequestParameters params) throws RestletApplicationException;
}
