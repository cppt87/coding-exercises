package com.buddybank.api.dataproviders;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPEntityUpdater<T> extends IDataProvider<T> {

	public T update(RequestParameters requestParams, T entity) throws RestletApplicationException;
}
