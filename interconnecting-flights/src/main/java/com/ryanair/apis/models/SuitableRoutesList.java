package com.ryanair.apis.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SuitableRoutesList<T> extends ArrayList<List<T>> implements Comparator<List<T>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(List<T> o1, List<T> o2) {
		return o2.size() - o1.size();
	}
}
