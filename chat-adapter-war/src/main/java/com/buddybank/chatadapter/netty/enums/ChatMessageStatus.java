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
public enum ChatMessageStatus {
	SENT_ERROR(-1),
	NOT_SENT(0),
	SENT_NOT_DELIVERED(1),
	SENT_DELIVERED(2);
	private final Integer value;
	private final static Map<Integer, ChatMessageStatus> CONSTANTS = new HashMap<Integer, ChatMessageStatus>();

	static {
		for (ChatMessageStatus c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private ChatMessageStatus(Integer value) {
		this.value = value;
	}

	public static ChatMessageStatus fromValue(Integer value) {
		ChatMessageStatus constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return constant;
		}
	}
}
