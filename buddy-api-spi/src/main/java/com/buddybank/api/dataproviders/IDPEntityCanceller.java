package com.buddybank.api.dataproviders;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPEntityCanceller<T> extends IDataProvider<T> {

	public T delete(RequestParameters params) throws RestletApplicationException;
}
