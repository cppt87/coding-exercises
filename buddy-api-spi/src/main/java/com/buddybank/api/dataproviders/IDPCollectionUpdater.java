package com.buddybank.api.dataproviders;

import java.util.Collection;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPCollectionUpdater<T> extends IDataProvider<T> {

	public Collection<T> update(RequestParameters params, Collection<T> collection) throws RestletApplicationException;
}
