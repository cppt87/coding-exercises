package com.buddybank.chatadapter.netty.server.dataproviders;

import java.io.IOException;

import com.buddybank.chatadapter.netty.server.utils.ChatSession;
import com.buddybank.chatadapter.netty.server.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

/**
 * @author c309844
 *
 */
public class HTTPDataProvider extends AbsDataProvider {

	public HTTPDataProvider(final IDPCallback callback, ChatSession session) {
		// super constructor
		super(callback, session);

		// Only first time
		Unirest.setObjectMapper(new ObjectMapper() {
			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return Utils.getMapper().readValue(value, valueType);
				} catch (IOException e) {
					callback.onError(new RuntimeException(e));
				}
				return null;
			}

			@Override
			public String writeValue(Object value) {
				try {
					return Utils.getMapper().writeValueAsString(value);
				} catch (JsonProcessingException e) {
					callback.onError(e);
				}
				return null;
			}
		});
		/*
		 * Timeouts setting up. First parameter is the time taken to connect to
		 * a server: 10000ms. Second parameter is time taken to receive data:
		 * 10000ms
		 */
		Unirest.setTimeouts(10000, 10000);
	}

	@Override
	public void run() {
		try {
			ObjectNode nextMsg = this.formatGenesysInputMsg();
			LOG.debug("Message to be sent Genesys: {}", nextMsg);
			// Object to Json
			HttpResponse<ObjectNode> postResponse = Unirest.post(Utils.GENESYS_TEST)
					.header("accept", "application/json").header("Content-Type", "application/json").body(nextMsg)
					.asObject(ObjectNode.class);
			LOG.debug("Message received from Genesys: {}", postResponse.getBody());
			// now nextMesg contains http response from Genesys: let's evaluate
			// it
			this.evaluateAndSendResponse(postResponse.getBody());
		} catch (Exception e) {
			this.callback.onError(e);
		}
	}
}
