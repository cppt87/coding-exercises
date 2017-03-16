package com.buddybank.api.logger;

import java.io.IOException;

import org.restlet.Context;
import org.restlet.Message;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;

import com.buddybank.SystemException;
import com.buddybank.api.utils.RestletHttpUtils;
import com.buddybank.logger.BBLogger;
import com.fasterxml.jackson.databind.JsonNode;

public class BBApiLogger extends BBLogger implements ApiLayerGeneralLogger {

	public BBApiLogger(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public ApiLayerGeneralLogger append(Request request) {
		append("method", request.getMethod().getName());
		append(" path", RestletHttpUtils.getRequestPath(request));
		append(" ip", request.getClientInfo().getAddress());
		append(" agent-name", request.getClientInfo().getAgentName());
		append(" agent-version", request.getClientInfo().getAgentVersion());
		if (request.isEntityAvailable()) {
			append(" entity", entity(request));
		}
		return this;
	}

	@Override
	public ApiLayerGeneralLogger append(Response response) {
		append("status", response.getStatus().getCode());
		append(" ip", response.getServerInfo().getAddress());
		if (response.isEntityAvailable()) {
			append(" entity", entity(response));
		}
		return this;
	}

	@Override
	public ApiLayerGeneralLogger append(HeaderModel header) {
		append("headers", header.toLogRow());
		return this;
	}


	@Override
	public ApiLayerGeneralLogger append(JsonNode payload) {
		if (payload != null) {
			append("payload", String.valueOf(payload));
		}
		return this;
	}

	/**
	 * workaround to read entity twice.
	 * see: https://github.com/restlet/restlet-framework-java/issues/593
	 */
	private String entity(Message requestOrResponse) {
		// Get entity
		Representation representation = requestOrResponse.getEntity();
		
		// Create entity backup
		String content = "";
		try {
			content = representation.getText();
		} catch (IOException e) {
			throw new SystemException(e, "error extracting entity from message");
		}
		MediaType mediaType = representation.getMediaType();
		StringRepresentation backup = new StringRepresentation(content, mediaType);

		// Set again the entity for the request
		requestOrResponse.setEntity(backup);
		
		// Returns entity as String
		return content;
	}
	
	// moved here from each RestApplication class
	public static Filter log(Class<? extends ServerResource> resourceClass, Context ctx) {
		Filter filter = new LogFilter(ctx);
		filter.setNext(resourceClass);
		return filter;
	}
}