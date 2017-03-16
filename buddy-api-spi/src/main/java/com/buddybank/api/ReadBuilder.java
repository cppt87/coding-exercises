package com.buddybank.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.restlet.data.Disposition;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.ClientErrorNotFoundException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.buddybank.api.exceptions.ServerErrorServiceUnavailableException;
import com.buddybank.utils.ReflectionUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class ReadBuilder<T extends Object> extends Builder<T> {

	protected EntityReader<T> entityReader;
	private CollectionReader<T> collectionReader;

	private List<Predicate<T>> filters = new ArrayList<>();

	public ReadBuilder(ServerResource resource) {
		super(resource);
		this.params = new RequestParameters(resource);
	}

	public ReadBuilder<T> setEntityReader(EntityReader<T> service) {
		this.entityReader = service;
		return this;
	}

	public ReadBuilder<T> setCollectionReader(CollectionReader<T> service) {
		this.collectionReader = service;
		return this;
	}

	public ReadBuilder<T> parseRequestAttribute(String attributeName) throws RestletApplicationException {
		addAttribute(attributeName);
		return this;
	}

	public void parseRequestBodyAttribute(T entity, String attributeName) throws RestletApplicationException {

		Object value = ReflectionUtils.getValue(entity, attributeName);
		LOG.info(String.format("attributeName %s=%s", attributeName, value));
		params.add(new RequestParam(RequestParamType.BODY, attributeName, value.toString()));

	}

	public ReadBuilder<T> parseQueryString() {
		Form queryParams = params.getResource().getQuery();
		Set<String> names = queryParams.getNames();
		for (String name : names) {
			LOG.debug(String.format("name %s", name));
			params.add(new RequestParam(RequestParamType.QUERY, name, queryParams.getFirstValue(name)));
		}
		return this;
	}

	public ReadBuilder<T> parseHeaderString() {
		Series<Header> headers = params.getResource().getRequest().getHeaders();
		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				String value = header.getValue();
				String name = header.getName();
				LOG.info(String.format("name %s", name));
				LOG.info(String.format("value %s", value));
				params.add(new RequestParam(RequestParamType.HEADER, name, value));
			}
		}
		return this;
	}

	public ReadBuilder<T> withCollectionFilter(Predicate<T> predicate) {
		filters.add(predicate);
		return this;
	}

	public ReadBuilder<T> validateAttribute(String msg, String attributeName) {
		super.prepareNotFoundError(msg, attributeName);
		return this;
	}

	public Representation readEntity() throws RestletApplicationException, JsonProcessingException {
		LOG.info("read entity for params={}", params);
		T entity = entityReader.read(params);

		if (entity == null) {
			this.emitNotFoundError();
		}

		LOG.info("//////////      entity=\n{}", entity);
		String result = super.serialize(entity, entityReader);
		return new StringRepresentation(result, MediaType.APPLICATION_JSON);
	}

	public Representation readCollection() throws RestletApplicationException, JsonProcessingException {
		List<RequestParam> queries = params.filter(RequestParamType.QUERY);
		List<RequestParam> attributes = params.filter(RequestParamType.ATTRIBUTE);
		Collection<T> collection = null;
		if ((queries != null && queries.size() > 0) || (attributes != null && attributes.size() > 0)) {
			collection = collectionReader.filter(params);
		} else if (filters.size() > 0) {
			Predicate<T> compositeFilter = Predicates.and(filters);
			collection = Collections2.filter(collectionReader.readAll(params.filter(RequestParamType.HEADER)),
					compositeFilter);
		} else {
			collection = collectionReader.readAll(params.filter(RequestParamType.HEADER));
		}
		String result = super.serialize(collection, collectionReader);
		return new StringRepresentation(result, MediaType.APPLICATION_JSON);
	}

	public void emitNotFoundError() throws ClientErrorNotFoundException {
		resource.setStatus(notFoundStatus);
		throw notFoundException;
	}

	// emit a new service unavailable exception 503
	private void emitServiceUnavailable(String serviceName) throws ServerErrorServiceUnavailableException {
		resource.setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
		throw new ServerErrorServiceUnavailableException(serviceName);
	}

	// read the availability of the resource serviceName
	public Representation readStatus() throws RestletApplicationException {
		LOG.info("read entity status for params={}", params);
		T entity = entityReader.read(params);

		if (entity == null) {
			this.emitServiceUnavailable(resource.getReference().getPath());
		}

		resource.setStatus(Status.SUCCESS_OK);
		return new EmptyRepresentation();
	}

	// read a media from filesystem and return it as representation
	public Representation readMedia() throws RestletApplicationException, JsonProcessingException {
		LOG.info("read media for params={}", params);
		T entity = entityReader.read(params);

		if (entity == null || !(entity instanceof File)) {
			this.emitNotFoundError();
		}

		FileRepresentation representation = new FileRepresentation((File) entity,
				new MediaType(new MimetypesFileTypeMap().getContentType((File) entity)));
		Disposition disp = new Disposition(Disposition.TYPE_ATTACHMENT);
		disp.setFilename(((File) entity).getName());
		disp.setSize(((File) entity).length());
		representation.setDisposition(disp);
		
		return representation;
	}
}
