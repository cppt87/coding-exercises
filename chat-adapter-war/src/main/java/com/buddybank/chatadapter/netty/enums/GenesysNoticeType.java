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
public enum GenesysNoticeType {
	NONE("0"),
	PUSH_URL("1"),
	TYPING_STARTED("2"),
	TYPING_STOPPED("3"),
	CUSTOM("4"),
	UPDATE_USER_DATA("5"),
	RESERVED("6"),
	PING("10"),
	USER_UPDATE_NICK("11"),
	MESSAGE_SENT("12");
	
	private final String value;
	private final static Map<String, GenesysNoticeType> CONSTANTS = new HashMap<String, GenesysNoticeType>();

	static {
		for (GenesysNoticeType c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	private GenesysNoticeType(String value) {
		this.value = value;
	}

	public static GenesysNoticeType fromValue(String value) {
		GenesysNoticeType constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
}
