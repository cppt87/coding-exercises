package com.buddybank.api;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.api.exceptions.RestletApplicationException;
import com.buddybank.api.utils.RestletHttpUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class BuddyBankResource extends ServerResource {

	private static final Logger LOG = LoggerFactory.getLogger(BuddyBankResource.class);
	
	public Representation emitError(RestletApplicationException e) {
		LOG.error("emitting error:", e);
		super.doError(e.getStatus(), e.getMessage());
		return null;
	}
	
	protected String getCorsAllowedMethods() {
		return "OPTIONS, GET, POST, PUT, PATCH, DELETE";
	}

	private boolean originAllowed(String origin) {
		if (origin.indexOf("http://localhost:") > -1) return true;
		List<String> allowedHosts = new ArrayList<>();
		/*
		if (autoEnableOrigin) {
			allowedHosts.add(origin);
		}
		*/
		return allowedHosts.contains(origin);
	}

	/**
     * set headers to allow CORS for AJAX.
	 * @param serverResource 
     */
    protected void setCorsHeaders(ServerResource resource) {
		Series<Header> headers = RestletHttpUtils.getResponseHeaders(resource.getResponse());
		if (headers == null) {
    		headers = new Series<Header>(Header.class);
    		@SuppressWarnings("unchecked")
			Series<Header> prev = (Series<Header>) resource.getResponse().getAttributes().putIfAbsent(HeaderConstants.ATTRIBUTE_HEADERS, headers);
    		if (prev != null) { headers = prev; }
		}

        // echo back Origin and Request-Headers
		Series<Header> requestHeaders = RestletHttpUtils.getRequestHeaders(resource.getRequest());
    	//LOG.info("   ----- requestHeaders = {}", requestHeaders);
        String origin = requestHeaders.getFirstValue("Origin", true);
        String auth = requestHeaders.getFirstValue("Authorization", true);

        if (originAllowed(origin) || hasAuthorizationHeader(auth)) {
        	//LOG.info("Origin " + origin + " Allowed");
	        if (origin == null) {
	            headers.add("Access-Control-Allow-Origin", "*");
	        } else {
	            headers.add("Access-Control-Allow-Origin", origin);
	        }
	        headers.add("Access-Control-Allow-Methods", getCorsAllowedMethods());

	        String allowHeaders = requestHeaders.getFirstValue("Access-Control-Request-Headers", true);
	        if (allowHeaders != null) {
	            headers.add("Access-Control-Allow-Headers", allowHeaders);
	        } else {
	        	List<String> allowedheaders = Lists.newArrayList("Accept",
	        			"Accept-Encoding", "Accept-Language", "Connection",
	        			"Content-Transfer-Encoding",
	        			"Content-Length", "Content-Type", "Host", "Origin",
	        			"Referer", "User-Agent", "Content-Transfer-Encoding");
	            headers.add("Access-Control-Allow-Headers", Joiner.on(",").join(allowedheaders));
	        }
	        headers.add("Access-Control-Allow-Credentials", "true");
	        headers.add("Access-Control-Max-Age", "60");
	        //LOG.info("OK headers={}", headers);
		} else {
			LOG.warn("Not Allowed Headers: " + requestHeaders);
		}
    }
	
	private boolean hasAuthorizationHeader(String auth) {
		return !Strings.isNullOrEmpty(auth);
	}
}
