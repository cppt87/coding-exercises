package com.buddybank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import com.google.common.io.Resources;

public class FileUtils {

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Resources.toByteArray(Resources.getResource(path));
		return new String(encoded, encoding);
	}

	public static String inputStreamToString(InputStream in) throws IOException {
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		StringBuilder responseStrBuilder = new StringBuilder();

		String inputStr;
		while ((inputStr = streamReader.readLine()) != null)
			responseStrBuilder.append(inputStr);
		return responseStrBuilder.toString();
	}

	public static File loadFile(String path, String newFilename) throws URISyntaxException {
		File f = loadFile(path);
		if (newFilename != null) {
			f.renameTo(new File(f.getParentFile(), newFilename));
		}

		return f;
	}

	public static File loadFile(String path) throws URISyntaxException {
		return (new File(Resources.getResource(path).getPath()));
	}
}
