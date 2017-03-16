package com.buddybank.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static List<String> bban(String bban, String pattern) {
		String[] templates = pattern.split(",");
		int start = 0;
		ArrayList<String> list = new ArrayList<String>();
		try {
			for (String tmp : templates) {
				String[] tt = tmp.split(" ");
				int len = 0;
				for (String t : tt) {
					int llen = Integer.parseInt(t.substring(0, t.length() - 1));
					len += llen;
				}
				int l = Math.min(len, bban.length());
				int ll = start + l;
				StringBuilder sb = new StringBuilder(l);
				for (int i = start; i < ll; i++) {
					char c = bban.charAt(i);
					sb.append(c);
				}
				list.add(sb.toString());
				start += l;
			}
		} catch (Throwable t) {
			return list;
		}
		return list;
	}

	public static String[] string(String string, String sep) {
		StringTokenizer st = new StringTokenizer(string, ",", false);
		return st.getTokens();
	}

	public static <T> String joinCollection(String separator, T [] collection) {
		StringBuilder sb = new StringBuilder();
		// collection.length - 1 => to not add separator at the end
		for (int i = 0; i < collection.length - 1; i++) {
			if (!collection[i].toString().matches(" *")) {
				// empty string are ""; " "; " "; and so on
				sb.append(collection[i]);
				sb.append(separator);
			}
		}
		sb.append(collection[collection.length - 1].toString().trim());
		return sb.toString();
	}
}
