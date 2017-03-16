package com.buddybank.api;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.fasterxml.jackson.databind.JsonNode;

public interface Deserializer<T> {

	public abstract T toObject(JsonNode node, String... params) throws JsonProcessingException, RestletApplicationException;
}