package com.buddybank.api.dataproviders;

import java.util.Collection;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPCollectionCreator<T> extends IDataProvider<T> {

	public Collection<T> create(RequestParameters requestParams, Collection<T> collection)
			throws RestletApplicationException;
}
