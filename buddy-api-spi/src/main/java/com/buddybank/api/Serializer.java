package com.buddybank.api;

import com.buddybank.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Serializer<T> {

	ObjectNode toJson(T entity, String... params) throws JsonProcessingException;

}