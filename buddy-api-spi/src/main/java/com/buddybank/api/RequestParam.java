package com.buddybank.api;

public class RequestParam {
	private RequestParamType type = RequestParamType.ATTRIBUTE;
	private String name;
	private String value;
	
	public RequestParam(RequestParamType type, String name, String value) {
		super();
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public RequestParamType getType() {
		return type;
	}

	public void setType(RequestParamType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("%s {%s=%s}", this.getClass().getName(), this.name, this.value);
	}
}
