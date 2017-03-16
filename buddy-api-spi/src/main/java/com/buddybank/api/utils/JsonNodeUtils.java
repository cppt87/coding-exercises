package com.buddybank.api.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.buddybank.utils.TimestampUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Resources;

/*
 * This class expose three method types:
 * 
 * - get<*>Value: return the value for the given field, casted to the type in the method name
 * - get<*>ValueOrFail: as the method above but throws Exception if the field is not present in the JSON node
 * - getMandatory<*>Value: as the method above but throws Exception if the field is empty or has no value
 */
public class JsonNodeUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JsonNodeUtils.class);

	public List<String> getStringsList(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		List<String> elements = new ArrayList<>();
		JsonNode nodeValue = getNodeValue(jsonNode, fieldName);
		if (nodeValue == null) {
			return elements;
		}
		if (nodeValue.isArray()) {
			for (final JsonNode objNode : nodeValue) {
				elements.add(objNode.asText());
			}
		}
		return elements;
	}

	public static List<JsonNode> loadRawListFromResource(String resourcePath) {
		List<JsonNode> rawList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			URL u = Resources.getResource(resourcePath);
			JsonNode jsonNode = objectMapper.readTree(Resources.toByteArray(u));
			if (jsonNode.isArray()) {
				for (JsonNode node : jsonNode) {
					rawList.add(node);
				}
			}
			return rawList;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Date getMandatoryISODate(JsonNode node, String fieldName) throws RestletApplicationException {
		String stringValue = getMandatoryStringValue(node, fieldName);
		try {
			return TimestampUtils.getDateForISO8601String(stringValue);
		} catch (ParseException e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "date", stringValue);
		}
	}

	public Date getMandatoryShortDate(JsonNode node, String fieldName) throws RestletApplicationException {
		String stringValue = getMandatoryStringValue(node, fieldName);
		try {
			return TimestampUtils.getDateForString(stringValue, "yyyy-MM-dd");
		} catch (ParseException e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "date", stringValue);
		}
	}

	public Date getDateValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		if (v == null || v.isNull()) {
			return null;
		} else {
			String s = v.asText();
			if (s.isEmpty()) {
				return null;
			} else {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date startDate;
				try {
					startDate = df.parse(s);
					String newDateString = df.format(startDate);
					System.out.println(newDateString);
					return startDate;
				} catch (ParseException e) {
					throw new ClientErrorUnprocessableEntityException(fieldName, "date", s);
				}
			}
		}
	}

	public int getIntValueOrFail(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		return getNodeValueOrFail(jsonNode, fieldName).asInt();
	}

	public Integer getIntValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		return getIntValue(jsonNode, fieldName, null);
	}

	public Integer getIntValue(JsonNode jsonNode, String fieldName, Integer defaultValue)
			throws ClientErrorUnprocessableEntityException {
		JsonNode nodeValue = getNodeValue(jsonNode, fieldName);
		if (nodeValue == null)
			return defaultValue;
		else
			return nodeValue.asInt();
	}
	
	public Double getDoubleValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		return getDoubleValue(jsonNode, fieldName, null);
	}

	public Double getDoubleValue(JsonNode jsonNode, String fieldName, Double defaultValue)
			throws ClientErrorUnprocessableEntityException {
		JsonNode nodeValue = getNodeValue(jsonNode, fieldName);
		if (nodeValue == null)
			return defaultValue;
		else
			return nodeValue.asDouble();
	}

	public Boolean getBooleanValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		JsonNode nodeValue = getNodeValue(jsonNode, fieldName);
		if (nodeValue == null)
			return Boolean.FALSE;
		else
			return nodeValue.asBoolean();
	}

	public Boolean getBooleanValueOrFail(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		return getNodeValueOrFail(jsonNode, fieldName).asBoolean();
	}

	public String getStringValueOrFail(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		return v.asText();
	}

	public String getMandatoryStringValue(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		if (v == null || v.isNull()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		String s = v.asText();
		if (s.isEmpty()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		return s;
	}

	public Date getMandatoryDateValue(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		if (v == null || v.isNull()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		String s = v.asText();
		// LOG.info("DATE {} s={}", fieldName, s);
		if (s.isEmpty()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		Date date = null;
		try {
			date = TimestampUtils.getDateForISO8601String(s);
			// LOG.info("DATE {} date={}", fieldName, date);
		} catch (ParseException e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "date", s);
		}
		return date;
	}

	public BigDecimal getMandatoryBigDecimalValue(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		if (v == null || v.isNull()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		try {
			// LOG.info("V double={} long={} str={}", v.asDouble(), v.asLong(),
			// v.asText());
			BigDecimal b = BigDecimal.valueOf(v.asDouble());
			return b;
		} catch (Exception e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "bigdecimal", v);
		}
	}

	public BigDecimal getBigDecimalValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValue(jsonNode, fieldName);
		if (v == null || v.isNull()) 
			return null;
		try {
			BigDecimal b = BigDecimal.valueOf(v.asDouble());
			return b;
		} catch (Exception e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "bigdecimal", v);
		}
	}
	
	/*
	 * Throws ClientErrorUnprocessableEntityException if the field is not
	 * present in the jsonNode.
	 */
	public JsonNode getNodeValueOrFail(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		if (!jsonNode.has(fieldName)) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		return jsonNode.get(fieldName);
	}

	public String getStringValue(JsonNode jsonNode, String fieldName) throws ClientErrorUnprocessableEntityException {
		JsonNode nodeValue = getNodeValue(jsonNode, fieldName);
		/*
		 * if (nodeValue == null) { return null; } String val = ""; try { val =
		 * new
		 * String(JsonStringEncoder.getInstance().encodeAsUTF8(nodeValue.asText
		 * ()), "UTF-8"); } catch (UnsupportedEncodingException e) { throw new
		 * ClientErrorUnprocessableEntityException("Encoding error"); } return
		 * val;
		 */
		if (nodeValue == null)
			return null;
		else
			return nodeValue.asText();
	}

	/*
	 * Returns the field value in a safe manner: if the field is not present in
	 * the jsonNode returns null.
	 */
	public JsonNode getNodeValue(JsonNode jsonNode, String fieldName) {
		if (!jsonNode.has(fieldName)) {
			return null;
		}
		return jsonNode.get(fieldName);
	}

	public void fillInMapWithObject(JsonNode node, String fieldName, Map<String, String> map)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValue(node, fieldName);
		if (v != null && map != null && JsonNodeType.OBJECT.equals(v.getNodeType())) {
			ObjectNode o = (ObjectNode) v;
			Set<String> keys = map.keySet();
			for (String key : keys) {
				LOG.warn(">> {}={}", key, map.get(key));
				o.put(key, map.get(key));
			}
		}
	}

	public Map<String, String> nodeFieldToMap(JsonNode node, String fieldName) {
		JsonNode v = getNodeValue(node, fieldName);
		Map<String, String> map = new HashMap<>();
		if (v != null && JsonNodeType.OBJECT.equals(v.getNodeType())) {
			for (Iterator<Entry<String, JsonNode>> it = v.fields(); it.hasNext();) {
				Entry<String, JsonNode> field = it.next();
				map.put(field.getKey(), field.getValue().asText());
			}
		}
		return map;
	}

	public ObjectNode mapToObject(Map<String, String> map) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		if (map == null || map.isEmpty()) {
			return obj;
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			obj.put(entry.getKey(), entry.getValue());
		}
		return obj;
	}

	public boolean getMandatoryBooleanValue(JsonNode node, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(node, fieldName);
		if (v == null || v.isNull()) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
		return v.asBoolean();
	}

	public BigDecimal getMandatoryMoneyValue(JsonNode node, String string) {
		throw new NotImplementedException("JsonNodeUtils.getMandatoryMoneyValue");
	}

	public ArrayNode getArrayNode(JsonNode jsonNode, String fieldName) {
		if (!jsonNode.has(fieldName)) {
			return null;
		}
		return (ArrayNode) jsonNode.get(fieldName);
	}

	public ArrayNode getMandatoryArrayNode(JsonNode jsonNode, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		JsonNode v = getNodeValueOrFail(jsonNode, fieldName);
		if (v == null || v.isNull() || v.size() == 0) {
			throw new ClientErrorUnprocessableEntityException(fieldName);
		}
	    ArrayNode arrayNode = null;
	    try {
	      arrayNode = (ArrayNode) v;
	    } catch (Exception e) {
	      throw new ClientErrorUnprocessableEntityException(fieldName);
	    }
	    return arrayNode;
	}

	public Locale getMandatoryLocaleValue(JsonNode node, String fieldName)
			throws ClientErrorUnprocessableEntityException {
		String localeId = getMandatoryStringValue(node, fieldName);
		try {
			return LocaleUtils.toLocale(localeId);
		} catch (IllegalArgumentException e) {
			throw new ClientErrorUnprocessableEntityException(fieldName, "locale", localeId);
		}
	}
}
