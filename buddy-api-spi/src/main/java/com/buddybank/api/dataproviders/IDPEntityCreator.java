package com.buddybank.api.dataproviders;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPEntityCreator<T> extends IDataProvider<T> {

	public T create(RequestParameters requestParams, T entity) throws RestletApplicationException;
}
