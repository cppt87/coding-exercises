package com.buddybank.api.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.utils.JsonNodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeUtilsStringFieldsTest {

	@Test(expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryStringThrowsExceptionForMissingField() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
		json.getMandatoryStringValue(node, "invalid");
	}

	@Test(expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryStringThrowsExceptionForNullValue() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	String v = null;
    	node.put("invalid", v);
		json.getMandatoryStringValue(node, "invalid");
	}

	@Test(expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryStringThrowsExceptionForEmptyValue() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	String v = "";
    	node.put("invalid", v);
		json.getMandatoryStringValue(node, "invalid");
	}
	
	@Test
	public void testMandatoryStringKeepsBlankValue() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	String v = " ";
    	node.put("blank", v);
		String actual = json.getMandatoryStringValue(node, "blank");
		assertThat(actual).as("blank value").isEqualTo(v);
	}
}
