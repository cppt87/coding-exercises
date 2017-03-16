package com.buddybank.api.logger;

import java.security.NoSuchAlgorithmException;

import com.buddybank.utils.UUIDUtils;

public class HttpRequestId {

	public static final String REQUESTID_ATTRIBUTE = "buddybank.api.request.id";
	
	private final String uuid;
	
	public HttpRequestId() {
		uuid = getUuid();
	}
	
	public String get() {
		return uuid;
	}
	
	private String getUuid() {
    	try {
			return UUIDUtils.getUUID("request.id");
		} catch (NoSuchAlgorithmException e) {
			// FIXME
			e.printStackTrace();
			return UUIDUtils.noop("request.id"+System.currentTimeMillis());
		}
	}
}
