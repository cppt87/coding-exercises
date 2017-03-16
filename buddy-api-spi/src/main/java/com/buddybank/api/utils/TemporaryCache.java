package com.buddybank.api.utils;

import java.util.HashMap;
import java.util.Map;

public class TemporaryCache {
	
	private static Map<String, String> schemaMap = new HashMap<>();
	
	public static void addNewSchema(String key, String value){
		schemaMap.put(key, value);
	}
	
	public static String getSchema(String key){
		return schemaMap.get(key);
	}

}
