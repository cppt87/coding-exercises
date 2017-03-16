package com.buddybank.api.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.restlet.resource.ServerResource;
import org.slf4j.LoggerFactory;

/**
 * Filter for viewing Request Parameters and Session Attributes in log
 */
public class RequestDebugging {
	protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestDebugging.class.getName());

	public static void log(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		StringBuilder sb = new StringBuilder();
		HttpServletRequest hreq = (HttpServletRequest) request;
		sb.append("\nRequest for uri: " + hreq.getRequestURI());
		
		sb.append("\nAuthType: " + hreq.getAuthType());
		sb.append("\nCharacterEncoding: " + hreq.getCharacterEncoding());
		sb.append("\nContentLength: " + hreq.getContentLength());
		sb.append("\nContentType: " + hreq.getContentType());
		sb.append("\nContextPath: " + hreq.getContextPath());
		sb.append("\nLocalAddr: " + hreq.getLocalAddr());
		sb.append("\nLocalName: " + hreq.getLocalName());
		sb.append("\nLocalPort: " + hreq.getLocalPort());
		sb.append("\nMethod: " + hreq.getMethod());
		sb.append("\nPathInfo: " + hreq.getPathInfo());
		sb.append("\nPathTranslated: " + hreq.getPathTranslated());
		sb.append("\nProtocol: " + hreq.getProtocol());
		sb.append("\nQueryString: " + hreq.getQueryString());
		sb.append("\nRemoteAddr: " + hreq.getRemoteAddr());
		sb.append("\nRemoteHost: " + hreq.getRemoteHost());
		sb.append("\nRemotePort: " + hreq.getRemotePort());
		sb.append("\nRemoteUser: " + hreq.getRemoteUser());
		sb.append("\nRequestedSessionId: " + hreq.getRequestedSessionId());
		sb.append("\nScheme: " + hreq.getScheme());
		sb.append("\nServerName: " + hreq.getServerName());
		sb.append("\nServerPort: " + hreq.getServerPort());
		//sb.append("\nServletContext: " + hreq.getServletContext());
		sb.append("\nLocale: " + hreq.getLocale().toString());
		sb.append("\nRequestUrl: " + hreq.getRequestURL().toString());
		
		if (hreq.getRequestURI().contains(".js")) {
			// nothing to do here
		} else {
			if (logger.isDebugEnabled()) {
				sb.append("\n");
				logRequestParameters(hreq, sb);
				logSessionAttributes(hreq, sb);
				logCookies(hreq, sb);
			}
		}
		logger.info(sb.toString());
	}

	private static void logCookies(HttpServletRequest request, StringBuilder sb) {
		sb.append("Cookies: \n");
		if (null != request.getCookies()) {
			for (Cookie cookie : request.getCookies()) {
				String description = "";
				try {
					description = BeanUtils.describe(cookie).toString();
				} catch (Exception e) {
					logger.error("BeanUtils Exception describing cookie", e);
				}
				sb.append(" " + description + "\n");
			}
		}
		sb.append("COOKIES END \n\n");
	}

	private static void logSessionAttributes(HttpServletRequest request,
			StringBuilder sb) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(request.getSession().getCreationTime());
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		sb.append("SESSION " + request.getSession().getId() + " created at: "
				+ dfm.format(cal.getTime()) + "\n");
		sb.append("SESSION ATTRIBUTES: \n");
		Map<String, Object> sortedAttrs = sortSessionAttributes(request);
		for (Map.Entry<String, Object> entry : sortedAttrs.entrySet()) {
			String description = "";
			try {
				description = BeanUtils.describe(entry.getValue()).toString();
			} catch (Exception e) {
				logger.error("BeanUtils Exception describing attribute "
						+ entry.getKey(), e);
			}
			sb.append(" " + entry.getKey() + ": " + description + "\n");
		}
		sb.append("SESSION ATTRIBUTES END \n\n");
	}

	private static void logRequestParameters(ServletRequest request, StringBuilder sb) {
		sb.append("REQUEST PARAMETERS:\n");
		Map<String, String[]> sortedParams = sortRequestParameters(request);
		for (Map.Entry<String, String[]> entry : sortedParams.entrySet()) {
			StringBuilder builder = new StringBuilder();
			for (String s : entry.getValue()) {
				builder.append(s);
				builder.append(", ");
			}
			sb.append(" " + entry.getKey() + ": " + builder.toString() + "\n");
		}
		sb.append("REQUEST PARAMETERS END \n\n");
	}

	private static Map<String, Object> sortSessionAttributes(HttpServletRequest request) {
		Map<String, Object> sortedAttrs = new TreeMap<String, Object>();
		Enumeration<String> attrEnum = request.getSession().getAttributeNames();
		while (attrEnum.hasMoreElements()) {
			String s = attrEnum.nextElement();
			sortedAttrs.put(s, request.getAttribute(s));
		}
		return sortedAttrs;
	}

	private static Map<String, String[]> sortRequestParameters(ServletRequest request) {
		Map<String, String[]> sortedParams = new TreeMap<String, String[]>();				
		Set<Map.Entry<String, String[]>> params = request.getParameterMap().entrySet();
		for (Map.Entry<String, String[]> entry : params) {
			sortedParams.put(entry.getKey(), entry.getValue());
		}
		return sortedParams;
	}

	public void destroy() {
		// nothing to implement here
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// nothing to implement here
	}

	public static void log(ServerResource resource) {
		logger.debug("RootRef:" + resource.getRequest().getRootRef().toString());
		logger.debug("Path:" + resource.getRequest().getResourceRef().getPath());
		logger.debug("Fragment:" + resource.getRequest().getResourceRef().getFragment());
		logger.debug("HierarchicalPart:" + resource.getRequest().getResourceRef().getHierarchicalPart());
		logger.debug("Identifier:" + resource.getRequest().getResourceRef().getIdentifier());
		logger.debug("LastSegment:" + resource.getRequest().getResourceRef().getLastSegment());
		logger.debug("RelativePart:" + resource.getRequest().getResourceRef().getRelativePart());
		logger.debug("RemainingPart:" + resource.getRequest().getResourceRef().getRemainingPart());
		logger.debug("Segments:" + resource.getRequest().getResourceRef().getSegments());
	}
}