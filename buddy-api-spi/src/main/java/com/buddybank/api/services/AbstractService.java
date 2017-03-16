package com.buddybank.api.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.RequestParam;
import com.buddybank.api.RequestParamType;
import com.buddybank.api.RequestParameters;
import com.buddybank.api.Rialize;
import com.buddybank.api.dataproviders.IDataProvider;
import com.buddybank.api.exceptions.ClientErrorBadRequestException;
import com.buddybank.api.exceptions.ClientErrorUnauthorizedException;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.buddybank.api.exceptions.ServerErrorInternalException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractService<T> {

	public abstract Rialize<T> getJsonRialize();

	public static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

	public abstract String getFakeListName();

	public abstract IDataProvider<T> getMDWDataProvider();

	public abstract IDataProvider<T> getFakeDataProvider();

	public ObjectNode toJson(T entity, String... params) throws JsonProcessingException {
		Rialize<T> rialize = getJsonRialize();
		return rialize.toJson(entity, params);
	}

	public T toObject(JsonNode node, String... params)
			throws JsonProcessingException, RestletApplicationException {
		Rialize<T> rialize = getJsonRialize();
		return rialize.toObject(node, params);
	}

	public IDataProvider<T> getDataProvider(List<RequestParam> headers) {
		for (RequestParam par : headers) {
			if ("X-BB-BACKEND".equalsIgnoreCase(par.getName()) && par.getValue().equalsIgnoreCase("FAKE")) {
				return getFakeDataProvider();
			}
		}
		return getMDWDataProvider();
	}

	public IDataProvider<T> getDataProvider(RequestParameters params) {
		if (!params.isEmpty() && !params.filter(RequestParamType.HEADER).isEmpty()) {
			return getDataProvider(params.filter(RequestParamType.HEADER));
		} else
			return getMDWDataProvider();
	}

	public String getAuthenticatedUserId(List<RequestParam> headers) throws RestletApplicationException {
		String authenticatedUserId = null;
		for (RequestParam param : headers) {
			if ("X-BB-USERID".equalsIgnoreCase(param.getName())) {
				authenticatedUserId = param.getValue();
			}
		}
		if (authenticatedUserId == null) {
			throw new ClientErrorUnauthorizedException();
		}
		return authenticatedUserId;
	}

	public String getDataPresentationType(List<RequestParam> headers) throws RestletApplicationException {
		String type = "full";
		for (RequestParam param : headers) {
			if ("Prefer".equalsIgnoreCase(param.getName())) {
				String val = param.getValue();
				if (val.contains("return=full"))
					type = "full";
				else if (val.contains("return=minimal"))
					type = "minimal";
				break;
			}
		}
		return type;
	}

	public JSONArray extractServiceParams(List<RequestParam> headers)
			throws RestletApplicationException, JSONException {
		JSONArray listOfEntry = new JSONArray();
		for (RequestParam param : headers) {
			if ("X-BB-HAC".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "SECURE_TOKEN");
				listOfEntry.put(entry);
			} else if ("X-BB-PUSHTOKEN".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "PUSH_TOKEN");
				listOfEntry.put(entry);
			} else if ("X-BB-LANGUAGE".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "APP_LANGUAGE");
				listOfEntry.put(entry);
			} else if ("X-BB-APPID".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "APP_ID");
				listOfEntry.put(entry);
			} else if ("X-BB-APPVERSION".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "APP_VERSION");
				listOfEntry.put(entry);
			} else if ("X-BB-CHANNEL".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "CHANNEL");
				listOfEntry.put(entry);
			} else if ("X-BB-DEVICETYPE".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "DEVICE_TYPE");
				listOfEntry.put(entry);
			} else if ("X-BB-REFRESHCACHE".equalsIgnoreCase(param.getName())) {
				JSONObject entry = new JSONObject();
				entry.put("value", param.getValue());
				entry.put("key", "REFRESH_CACHE");
				listOfEntry.put(entry);
			}
		}
		return listOfEntry;
	}

	public void handleError(String faultjson) throws ClientErrorUnprocessableEntityException,
			ClientErrorBadRequestException, ClientErrorUnauthorizedException, ServerErrorInternalException {
		try {
			JSONObject foult = new JSONObject(faultjson);
			if (!foult.has("detail")) {
				throw new ServerErrorInternalException(new Exception(faultjson));
			}
			JSONObject detail = foult.getJSONObject("detail");
			JSONObject detail1 = detail.getJSONObject("FaultInfo");
			JSONObject detail2 = null;
			try {
				detail2 = detail1.getJSONObject("faults");
			} catch (Exception e) {
				detail2 = detail1.getJSONArray("faults").getJSONObject(0);
			}
			String foultLevel = detail2.getString("faultLevel");
			if ("UI_RECOVERABLE".equalsIgnoreCase(foultLevel)) {
				throw new ClientErrorUnprocessableEntityException(faultjson);
			} else if ("BUSINESS".equalsIgnoreCase(foultLevel)) {
				throw new ClientErrorBadRequestException(new Exception(faultjson));
			} else if ("SYSTEM_UNAVAILABLE".equalsIgnoreCase(foultLevel)) {
				throw new ClientErrorUnauthorizedException(new Exception(faultjson));
			} else if ("FATAL".equalsIgnoreCase(foultLevel)) {
				throw new ServerErrorInternalException(new Exception(faultjson));
			} else {
				throw new ServerErrorInternalException(new Exception(faultjson));
			}

		} catch (JSONException e1) {
			throw new ServerErrorInternalException(e1);
		}
	}
}
