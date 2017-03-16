package com.buddybank.api;

import com.buddybank.api.exceptions.RestletApplicationException;

public interface RequestParser {

	void addAttribute(String key) throws RestletApplicationException;

}