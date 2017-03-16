package com.buddybank.api.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.utils.JsonNodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeUtilsLocaleFieldsTest {

	@Test(expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryLocaleThrowsExceptionForMissingField() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
		json.getMandatoryLocaleValue(node, "invalid");
	}

	@Test(expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryLocaleThrowsExceptionForNullValue() throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	String v = null;
    	node.put("invalid", v);
		json.getMandatoryLocaleValue(node, "invalid");
	}
	
	@Test(dataProvider="invalidLocales", expectedExceptions=ClientErrorUnprocessableEntityException.class)
	public void testMandatoryLocaleThrowsExceptionForInvalidField(String id, Locale expected) throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put("localfield", id);
		json.getMandatoryLocaleValue(node, "localfield");
	}
	
	@Test(dataProvider="validLocales")
	public void testMandatoryLocaleHappyPath(String id, Locale expected) throws Exception {
		JsonNodeUtils json = new JsonNodeUtils();
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put("localfield", id);
		Locale actual = json.getMandatoryLocaleValue(node, "localfield");
		assertThat(actual).as("locale "+id).isEqualTo(expected);
	}
	
	@DataProvider(name="validLocales")
	public Object[][] validLocalesDataProvider() {
		return new Object[][]{
			{ "it", Locale.ITALIAN },
			{ "it_IT", Locale.ITALY }
		};
	}
	
	@DataProvider(name="invalidLocales")
	public Object[][] invalidLocalesDataProvider() {
		return new Object[][]{
			{ null, Locale.ITALY },
			{ "", Locale.ITALY },
			{ "it-it", Locale.ITALY },
			{ "IT_it", Locale.ITALY },
			{ "IT", Locale.ITALY },
			{ "not a valid locale", Locale.ITALY }
		};
	}
}
