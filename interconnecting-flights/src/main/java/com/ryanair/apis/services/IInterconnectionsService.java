package com.ryanair.apis.services;

import java.time.LocalDateTime;
import java.util.Deque;

public interface IInterconnectionsService<T> {
	public Deque<T> getInterconnections(String departure, String arrival, LocalDateTime departureTime,
			LocalDateTime arrivalTime) throws Exception;
}
