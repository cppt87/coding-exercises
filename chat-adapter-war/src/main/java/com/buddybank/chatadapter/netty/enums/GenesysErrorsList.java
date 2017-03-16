/**
 * 
 */
package com.buddybank.chatadapter.netty.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author c309844
 *
 */
public enum GenesysErrorsList {
	NO_ERROR(0),
	SECURITY_VIOLATION(1),
	COMMAND_UNKNOWN(2),
	CONNECTION_LOST(3),
	CHAT_CLOSED(4),
	CONNECTION_ERROR(5),
	GENERIC_ERROR(6),
	REQUEST_ERROR(7),
	RESPONSE_ERROR(8);
	
	private final Integer value;
	private final static Map<Integer, GenesysErrorsList> CONSTANTS = new HashMap<Integer, GenesysErrorsList>();

	static {
		for (GenesysErrorsList c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private GenesysErrorsList(Integer value) {
		this.value = value;
	}

	public static GenesysErrorsList fromValue(Integer value) {
		GenesysErrorsList constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return constant;
		}
	}
}
