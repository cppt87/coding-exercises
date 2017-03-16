package com.buddybank.api.dataproviders;

import java.util.Collection;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPCollectionCanceller<T> extends IDataProvider<T> {

	public Collection<T> delete(RequestParameters params, Collection<T> collection) throws RestletApplicationException;
}
