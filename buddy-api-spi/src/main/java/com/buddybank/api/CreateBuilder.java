package com.buddybank.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

public class CreateBuilder<T> extends Builder<T> {

	static final Logger LOG = LoggerFactory.getLogger(CreateBuilder.class);

	private EntityCreator<T> entityCreator;
	private CollectionCreator<T> collectionCreator;
	private JsonNode node;
	private RepresentationType type;
	private T entity;
	private Collection<T> collection;
	private T responseEntity;
	private Collection<T> responseCollection;

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public CreateBuilder(ServerResource resource) {
		super(resource);
		this.params = new RequestParameters(resource);
	}

	public CreateBuilder<T> setEntityCreator(EntityCreator<T> service) {
		this.entityCreator = service;
		return this;
	}

	public CreateBuilder<T> setCollectionCreator(CollectionCreator<T> service) {
		this.collectionCreator = service;
		return this;
	}

	public CreateBuilder<T> parseRequestAttribute(String attributeName) throws RestletApplicationException {
		addAttribute(attributeName);
		return this;
	}

	public CreateBuilder<T> parse(JsonRepresentation representation)
			throws JsonProcessingException, RestletApplicationException {
		node = super.deserialize(representation);
		type = super.checkType(node);
		LOG.info("parse {} : {}", type, node);
		
		switch (type) {
		case ENTITY:
			entity = parseEntity(node, entityCreator);
			break;
		case COLLECTION:
			collection = parseCollection(node, collectionCreator);
			break;
		}
		return this;
	}

	public CreateBuilder<T> parseHeaderString() {
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

	public CreateBuilder<T> validateAttribute(String msg, String attributeName) {
		super.prepareNotFoundError(msg, attributeName);
		return this;
	}

	public CreateBuilder<T> create() throws RestletApplicationException {
		switch (type) {
		case ENTITY:
			responseEntity = entityCreator.create(params, entity);
			break;
		case COLLECTION:
			responseCollection = collectionCreator.create(params, collection);
			break;
		}
		logRequest();
		logRequestHeaders();
		logPayload(node);
		logEntity(entity);
		return this;
	}

	public Representation redirectSeeOther() throws JsonProcessingException, MalformedURLException {
		String location = params.get("Location").getValue();
		resource.redirectSeeOther(new Reference(new URL(location)));
		logResponseRedirect(location);
		return new EmptyRepresentation();
	}

	public Representation returnResults() throws JsonProcessingException {
		String result = null;
		switch (type) {
		case ENTITY:
			result = serialize(responseEntity, entityCreator);

			break;
		case COLLECTION:
			result = serialize(responseCollection, collectionCreator);
			break;
		}

		resource.setStatus(Status.SUCCESS_CREATED);
		logResponse(Status.SUCCESS_CREATED.getCode(), result);
		return new StringRepresentation(result, MediaType.APPLICATION_JSON);
	}
}
