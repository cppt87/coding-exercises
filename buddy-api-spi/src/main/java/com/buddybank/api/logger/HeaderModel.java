package com.buddybank.api.logger;

import org.restlet.Message;
import org.restlet.data.Header;
import org.restlet.util.Series;

public class HeaderModel {
	
	private StringBuilder sb = new StringBuilder();

	public HeaderModel(Message requestOrResponse) {
		if (requestOrResponse == null) {
			sb.append("{}");
			return;
		}
//		boolean first = true;
		sb.append("{").append('"').append("implementation").append('"').append(':').append('"').append(requestOrResponse.getClass().getName()).append('"');
		Series<Header> headers = requestOrResponse.getHeaders();
		for (Header header : headers) {
//			if (first) {
//				first = false;
//			} else {
				sb.append(", ");
//			}
			sb.append('"').append(header.getName()).append('"').append(":").append('"').append(header.getValue()).append('"');
		}
		sb.append("}");
	}
	
	public String toLogRow() {
		return sb.toString();
	}
}
