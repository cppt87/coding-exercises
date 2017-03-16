package com.buddybank.api;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.ClientErrorBadRequestException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

public class ActionBuilder<T> extends ReadBuilder<T> {
	
	static final Logger LOG = LoggerFactory.getLogger(ActionBuilder.class);

	private EntityAction<T> entityAction;
	private JsonNode node;
	private RepresentationType type;
	private T entity;
	private T responseEntity;
	
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public ActionBuilder(ServerResource resource) {
		super(resource);
	}

	public ActionBuilder<T> parseRequestAttribute(String attributeName) throws RestletApplicationException {
		super.parseRequestAttribute(attributeName);
		return this;
	}
	
	public ActionBuilder<T> parseHeaderString() {
		super.parseHeaderString();
		return this;
	}
	
	public ActionBuilder<T> parseQueryString() {
		super.parseQueryString();
		return this;
	}

	public ActionBuilder<T> parseRequestBodyAttribute(String attributeName) throws RestletApplicationException {
		super.parseRequestBodyAttribute(entity, attributeName);
		return this;
	}
	
	public ActionBuilder<T> validateAttribute(String msg, String attributeName) {
		super.validateAttribute(msg, attributeName);
		return this;
	}
	
	
	public ActionBuilder<T> setEntityAction(EntityAction<T> service) {
		super.setEntityReader(service);
		this.entityAction = service;
		return this;
	}

	public ActionBuilder<T> parse(JsonRepresentation representation) throws JsonProcessingException, RestletApplicationException {
		node = super.deserialize(representation);
		type = super.checkType(node);
		LOG.info("parse {} : {}", type, node);
		switch (type) {
		case ENTITY:
			entity = super.parseEntity(node, entityAction);
			break;
		case COLLECTION:
			throw new ClientErrorBadRequestException();
		}
		return this;
	}

	public ActionBuilder<T> doAction(String actionId) throws RestletApplicationException {
		T entity = entityAction.read(params);
		
		if (entity == null) {
			super.emitNotFoundError();
		}

		this.responseEntity = entityAction.doAction(entity, actionId, params);
		
		return this;		
	}

	public Representation returnResults() throws JsonProcessingException {
		String result =  serialize(responseEntity, entityAction);		
		resource.setStatus(Status.SUCCESS_CREATED);
		return new StringRepresentation(result, MediaType.APPLICATION_JSON);
	}

}
