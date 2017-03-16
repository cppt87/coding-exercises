package com.buddybank.api.logger;

import org.restlet.Request;
import org.restlet.Response;

import com.buddybank.logger.GeneralLogger;
import com.fasterxml.jackson.databind.JsonNode;

public interface ApiLayerGeneralLogger extends GeneralLogger {
	public ApiLayerGeneralLogger append(Request request);
	public ApiLayerGeneralLogger append(Response response);
	public ApiLayerGeneralLogger append(HeaderModel header);
	public ApiLayerGeneralLogger append(JsonNode payload);
}
