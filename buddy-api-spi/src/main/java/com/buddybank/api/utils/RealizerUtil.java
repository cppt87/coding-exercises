package com.buddybank.api.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RealizerUtil {

	public static String getStringDate(Date valueDate) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String result = df.format(valueDate);
		return result;
	}

	public static Date estractDate(JsonNode node, String name, JsonNodeUtils json)
			throws JsonProcessingException, ClientErrorUnprocessableEntityException {
		try {
			String date = json.getStringValue(node, name);
			if (date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				Date dt = sdf.parse(date);
				return dt;
			}
			return null;
		} catch (ParseException e) {
			throw new JsonProcessingException(e);
		}
	}

	// public static String getStringTimestamp(Date valueDate){
	// DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	// String result =df.format(valueDate);
	// return result;
	// }
	//
	// public static Date estractTimestamp(JsonNode node,String
	// name,JsonNodeUtils json) throws
	// JsonProcessingException,ClientErrorUnprocessableEntityException {
	// try{
	// String date= json.getStringValue(node, name);
	// if (date!=null){
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	// Date dt = sdf.parse(date);
	// return dt;
	// }
	// return null;
	// } catch (ParseException e) {
	// throw new JsonProcessingException(e);
	// }
	// }

	public static ObjectNode getObjectNode() {
		ObjectNode responseObj = new ObjectMapper().createObjectNode();
		return responseObj;
	}

	public static ArrayNode getArrayNode() {
		ArrayNode arrayObj = new ObjectMapper().createArrayNode();
		return arrayObj;
	}

}
