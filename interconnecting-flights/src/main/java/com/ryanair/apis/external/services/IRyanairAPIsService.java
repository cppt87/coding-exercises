package com.ryanair.apis.external.services;

import com.ryanair.apis.external.models.RyanairRouteResource;
import com.ryanair.apis.external.models.RyanairScheduleResource;

public interface IRyanairAPIsService {
	/*
	 * returns a list of all available routes based on the airport's IATA codes
	 */
	RyanairRouteResource[] routesAPI() throws Exception;

	/*
	 * returns a list of available flights for a given departure airport IATA
	 * code, an arrival airport IATA code, a year and a month
	 */
	RyanairScheduleResource schedulesAPI(String departure, String arrival, int year, int month) throws Exception;
}
