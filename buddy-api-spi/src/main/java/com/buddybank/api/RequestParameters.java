package com.buddybank.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.resource.ServerResource;

public class RequestParameters {
	private HashMap <String, RequestParam> list = new HashMap<String, RequestParam>();
	private ServerResource resource;
	
	public RequestParameters(ServerResource resource) {
		super();
		this.resource = resource;
	}
	
	public ServerResource getResource() {
		return resource;
	}

	public void setResource(ServerResource resource) {
		this.resource = resource;
	}

	public void add(RequestParamType type, String name, String value) {
		if (!list.containsKey(name)) {
			list.put(name, new RequestParam(type, name, value));
		}
	}
	
	public void add(RequestParam requestParam) {
		if (!list.containsKey(requestParam.getName())) {
			list.put(requestParam.getName(), requestParam);
		}
	}
	
	public RequestParam get(String name) {
		return list.get(name);
	}
	
	public List<RequestParam> filter(RequestParamType type) {
		ArrayList<RequestParam> ll = new ArrayList<RequestParam>();
		Collection<RequestParam> values = list.values();
		for (RequestParam v : values) {
			if (v.getType().equals(type)) {
				ll.add(v);
			}
		}
		return ll;
	}	
	
	public boolean exists(String name) {
		return list.containsKey(name);
	}
	
	public void delete(String name) {
		list.remove(name);
	}
	
	public void delete(RequestParam requestParam) {
		list.remove(requestParam.getName());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(" {");
		for(Map.Entry<String, RequestParam> entry: list.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
		}
		sb.append(" }");
		return sb.toString();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}
