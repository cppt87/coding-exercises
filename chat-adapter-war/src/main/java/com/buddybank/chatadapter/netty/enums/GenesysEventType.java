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
public enum GenesysEventType {
	CONNECT("0"),
	MESSAGE("1"),
	ABANDON("2"),
	NOTICE("3");
	
	private final String value;
	private final static Map<String, GenesysEventType> CONSTANTS = new HashMap<String, GenesysEventType>();

	static {
		for (GenesysEventType c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private GenesysEventType(String value) {
		this.value = value;
	}

	public static GenesysEventType fromValue(String value) {
		GenesysEventType constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
}
