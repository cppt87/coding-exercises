package com.buddybank.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.AssertionFailedException;
import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.ClientErrorBadRequestException;
import com.buddybank.api.exceptions.ClientErrorNotFoundException;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.buddybank.api.exceptions.ServerErrorInternalException;
import com.buddybank.api.logger.BBApiLogger;
import com.buddybank.api.logger.HeaderModel;
import com.buddybank.api.utils.RestletHttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Builder<T> implements RequestParser {

	static final Logger LOG = LoggerFactory.getLogger(Builder.class);
	
	static final BBApiLogger APILOG = new BBApiLogger(Builder.class);
	
	protected ServerResource resource;
	protected RequestParameters params;
	public static final ObjectMapper mapper = new ObjectMapper();

	protected Status notFoundStatus = Status.CLIENT_ERROR_NOT_FOUND;
	protected ClientErrorNotFoundException notFoundException;
	
	public static ObjectMapper getMapper() {
		return mapper;
	}

	public Builder(ServerResource resource) {
		super();
		this.resource = resource;
	}

	protected T parseEntity(JsonNode node, Deserializer<T> deserializer) throws RestletApplicationException {   
		LOG.debug("parseEntity deser={} node={}", deserializer, node);
		try {
			return deserializer.toObject(node);
		} catch (JsonProcessingException e) {
			throw new ClientErrorBadRequestException(e);
		}
	}

	public Collection<T> parseCollection(JsonNode node, Deserializer<T> deserializer) throws RestletApplicationException {
		try {
			ArrayList<T> list = new ArrayList<>();
			Iterator<JsonNode> items = node.iterator();
			while (items.hasNext()) {
				list.add(deserializer.toObject(items.next()));
			}
			return list;
		} catch (JsonProcessingException e) {
			throw new ClientErrorBadRequestException(e);
		}			
	}

	public void prepareNotFoundError(String msg, String attributeName) {
		notFoundException = new ClientErrorNotFoundException(msg, attributeName);
	}
	
	public String serialize(T entity, Serializer<T> serializer, String... params) throws JsonProcessingException {		
		return serialize(serializer.toJson(entity, params));
	}

	public String serialize(Collection<T> list, Serializer<T> serializer, String... params) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayObj = mapper.createArrayNode();
		for (T item : list) {
			ObjectNode obj = serializer.toJson(item, params);
			arrayObj.add(obj);
		}
		return this.serialize(arrayObj);
	}

	public JsonNode deserialize(JsonRepresentation entity) throws RestletApplicationException {
	    try {
			return getMapper().readTree(entity.getReader());
		} catch (IOException e) {
			throw new ServerErrorInternalException(e);
		} catch (Throwable t) {
			throw new ServerErrorInternalException(t);
		}
	}

	public RepresentationType checkType(JsonNode node) throws JsonProcessingException {
		if (node.isArray()) {
			return RepresentationType.COLLECTION;
		} else {
			return RepresentationType.ENTITY;
		}		
	}

	protected String serialize(ObjectNode responseObj) throws JsonProcessingException {
		LOG.debug("serialize object={}", responseObj);
		if (responseObj == null) {
			return "{}";
		}
		try {
			return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseObj);
		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			throw new JsonProcessingException(e);
		}
	}

	protected String serialize(ArrayNode responseObj) throws JsonProcessingException {
		LOG.debug("serialize object={}", responseObj);
		if (responseObj == null) {
			return "{}";
		}
		try {
			return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseObj);
		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			throw new JsonProcessingException(e);
		}
	}

	@Override
	public void addAttribute(String key) throws RestletApplicationException {
		Object param = params.getResource().getRequest().getAttributes().get(key);
		LOG.debug("putIdentifier {}='{}'", key, param);
		if (param == null)
			throw new ServerErrorInternalException(new AssertionFailedException("Parameter " + key + " is null"));
		String paramValue = params.getResource().getRequest().getAttributes().get(key).toString();
		try {
			paramValue = URLDecoder.decode(paramValue, "UTF-8");
			paramValue = paramValue.replaceAll("§", "/");
			//LOG.info("{}={}", paramName, paramValue);
			if ((paramValue == null) || ("".equals(paramValue))) {
				throw new ClientErrorUnprocessableEntityException(key);
			}
			params.add(RequestParamType.ATTRIBUTE, key, paramValue);			
		} catch (UnsupportedEncodingException e) {
			throw new ServerErrorInternalException(e);
		}
	}
	
	protected String requestId() {
		return RestletHttpUtils.getRequestId(resource.getRequest());
	}
	
	protected void logRequest() {
    	APILOG.log();
    	APILOG.append(resource.getRequest());
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logRequest");
    	APILOG.warn();
	}

	protected void logResponse() {
    	APILOG.log();
    	APILOG.append(resource.getResponse());
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logResponse");
    	APILOG.warn();
	}

	protected void logRequestHeaders() {
		HeaderModel headerModel = new HeaderModel(resource.getRequest());
    	APILOG.log();
    	APILOG.append(headerModel);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logRequestHeaders");
    	APILOG.warn();
	}

	protected void logResponseHeaders() {
		HeaderModel headerModel = new HeaderModel(resource.getResponse());
    	APILOG.log();
    	APILOG.append(headerModel);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logResponseHeaders");
    	APILOG.warn();
	}
	
	protected void logPayload(JsonNode payload) {
    	APILOG.log();
    	APILOG.append(payload);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logPayload");
    	APILOG.warn();
	}
	
	protected void logEntity(T payload) {
    	APILOG.log();
    	APILOG.append(payload);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logEntity");
    	APILOG.warn();
	}

	protected void logResponse(int statusCode, String content) {
    	APILOG.log();
    	APILOG.append("response");
    	APILOG.append(" body", content);
    	APILOG.append(" status", statusCode);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logResponseBody");
    	APILOG.warn();
	}

	protected void logResponseRedirect(String location) {
    	APILOG.log();
    	APILOG.append("responseredirect", location);
    	APILOG.append(" requestid", requestId());
    	APILOG.append(" method", "logResponseRedirect");
    	APILOG.warn();
	}
}