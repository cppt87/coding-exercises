package com.buddybank.api.dataproviders;

import java.util.Collection;
import java.util.List;

import com.buddybank.api.RequestParam;
import com.buddybank.api.RequestParameters;
import com.buddybank.api.exceptions.RestletApplicationException;

public interface IDPCollectionReader<T> extends IDataProvider<T> {

	public Collection<T> readAll(List<RequestParam> headers) throws RestletApplicationException;

	public Collection<T> filter(RequestParameters queries) throws RestletApplicationException;
}
