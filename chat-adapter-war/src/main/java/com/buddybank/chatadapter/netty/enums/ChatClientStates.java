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
public enum ChatClientStates {
	DISCONNECTED(-1),
	CONNECTING(0),
	LOGIN_MSG_RECEIVED(1),
	CONNECTED_SOCK_CLS(2),
	CONNECTED_SOCK_OPN(3);
	
	private final Integer value;
	private final static Map<Integer, ChatClientStates> CONSTANTS = new HashMap<Integer, ChatClientStates>();

	static {
		for (ChatClientStates c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private ChatClientStates(Integer value) {
		this.value = value;
	}

	public static ChatClientStates fromValue(Integer value) {
		ChatClientStates constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return constant;
		}
	}
}
