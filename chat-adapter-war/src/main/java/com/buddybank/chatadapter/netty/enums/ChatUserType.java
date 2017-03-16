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
public enum ChatUserType {
	CLIENT("0"),
	AGENT("1"),
	SUPERVISOR("2"),
	EXTERNAL("3");
	
	private final String value;
	private final static Map<String, ChatUserType> CONSTANTS = new HashMap<String, ChatUserType>();

	static {
		for (ChatUserType c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private ChatUserType(String value) {
		this.value = value;
	}

	public static ChatUserType fromValue(String value) {
		ChatUserType constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
}
