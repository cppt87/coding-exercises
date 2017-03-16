/**
 * 
 */
package com.buddybank.chatadapter.netty.server.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.buddybank.api.utils.JsonNodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author c309844
 *
 */
public final class Utils {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final JsonNodeUtils json = new JsonNodeUtils();
	private static final String PROPERTIES_FILE = "chat_adapter_config.properties";
	private static Properties PROPERTIES;
	public static final int DEFAULT_THREADS_NUMBER = Math.max(1, Runtime.getRuntime().availableProcessors() * 2);
	public static final int DEFAULT_EXECUTOR_THREADS_NUMBER = 16;
	public static final String OS = System.getProperty("os.name");
	public static final String GENESYS_TEST = "http://vm30.bbank.it/ChatServer/Command";
	// public static final String GENESYS_PROD =
	// "http://vm30.bbank.it/ChatServer/Command";
	public static final int MAX_FRAME_SIZE = 4096;
	public static final int BUFFER_LOW_WATERMARK = 8192;
	public static final int BUFFER_HIGH_WATERMARK = 32768;

	/**
	 * 
	 */

	/**
	 * @return the json
	 */
	public static JsonNodeUtils getJson() {
		return json;
	}

	/**
	 * @return the mapper
	 */
	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static Properties getProperties() throws Exception {
		if (PROPERTIES == null) {
			InputStream inputStream;
			try {
				inputStream = Utils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
				if (inputStream != null) {
					PROPERTIES = new Properties();
					PROPERTIES.load(inputStream);
					inputStream.close();
				} else {
					throw new FileNotFoundException(
							"Property file '" + PROPERTIES_FILE + "' not found in the classpath");
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			}
		}

		return PROPERTIES;
	}
}
