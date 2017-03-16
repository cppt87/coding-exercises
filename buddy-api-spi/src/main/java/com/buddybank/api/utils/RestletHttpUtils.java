package com.buddybank.api.utils;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.util.Series;

import com.buddybank.api.logger.HttpRequestId;
import com.google.common.base.Strings;

import static com.buddybank.api.logger.HttpRequestId.REQUESTID_ATTRIBUTE;

public class RestletHttpUtils {

    public static String getRequestId(Request httpRequest) {
        Object value = httpRequest.getAttributes().get(REQUESTID_ATTRIBUTE);
        if (value instanceof HttpRequestId) {
        	return ((HttpRequestId) value).get();
        }
        // FIXME: TBD
        return (value == null ? "" : String.valueOf(value));
    }
	
    public static String getRequestPathParam(Request httpRequest, String key) {
        Object value = httpRequest.getAttributes().get(key);
        return (value == null ? "" : String.valueOf(value));
    }

    public static String getRequestPath(Request httpRequest) {
        String requestPath = httpRequest.getResourceRef().getPath();
        return Strings.nullToEmpty(requestPath);
    }

    public static String getRequestHeader(Request httpRequest, String headerKey) {    	
    	Series<Header> headers = httpRequest.getHeaders();
    	return (headers.getFirst(headerKey) == null ? "" : headers.getFirst(headerKey).toString());
    }
	
	@SuppressWarnings("unchecked")
	public static Series<Header> getRequestHeaders(Request httpRequest) {
		return  (Series<Header>) httpRequest.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
	}
	
	@SuppressWarnings("unchecked")
	public static Series<Header> getResponseHeaders(Response response) {
		return  (Series<Header>) response.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
	}
}
