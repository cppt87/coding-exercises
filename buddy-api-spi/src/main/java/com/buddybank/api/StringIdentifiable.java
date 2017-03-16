package com.buddybank.api;

import com.buddybank.Identifiable;

public class StringIdentifiable implements Identifiable<String> {

	private static final long serialVersionUID = -5773154249938475467L;

	private String value;
	
	public StringIdentifiable(String value) {
		super();
		this.value = value;
	}

	@Override
	public String getId() {
		return value;
	}

	@Override
	public void setId(String id) {
		value = id;
	}

}
