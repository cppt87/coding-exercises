package com.buddybank.api.services;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.utils.JsonNodeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractJsonRialize implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Logger LOG = LoggerFactory.getLogger(AbstractJsonRialize.class);
	
	public ObjectNode toJson(ObjectNode account, String... params) throws JsonProcessingException {
		LOG.info("[toJson] source={}", account);		
		return account; 
	} 
	
	public ObjectNode getObjectNode() {
		ObjectMapper mapper = getMapper();
		ObjectNode responseObj = mapper.createObjectNode();
		return responseObj;
	}

	public ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}
	
	public String getStringDate(Date valueDate){
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String result =df.format(valueDate);
		return result;
	}
	
	public ArrayNode getArrayNode() {
		ObjectMapper mapper = getMapper();
		ArrayNode arrayObj = mapper.createArrayNode();
		return arrayObj;
	}
	
	public Date getDatetoString(JsonNode node,String name,JsonNodeUtils json) throws JsonProcessingException,ClientErrorUnprocessableEntityException {
		try{
		String date= json.getStringValue(node, name);
		LOG.info("getDatetoString date : -----------------> {}",date);
		 if (date!=null){
			 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			 Date dt = sdf.parse(date);			
			 return dt;
		 }
		 return null;
		} catch (ParseException e) {
			throw new JsonProcessingException(e);
		}
	}
}