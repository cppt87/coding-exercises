package com.buddybank.api.dataproviders;

import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPEntityReader<T> extends IDataProvider<T> {

	public T read(RequestParameters params) throws RestletApplicationException;
}