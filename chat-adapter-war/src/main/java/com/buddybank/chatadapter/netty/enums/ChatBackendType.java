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
public enum ChatBackendType {
	FAKE(0),
	REAL(1);
	
	private final Integer value;
	private final static Map<Integer, ChatBackendType> CONSTANTS = new HashMap<Integer, ChatBackendType>();

	static {
		for (ChatBackendType c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private ChatBackendType(Integer value) {
		this.value = value;
	}

	public static ChatBackendType fromValue(Integer value) {
		ChatBackendType constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return constant;
		}
	}
}
