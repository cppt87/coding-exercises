package com.buddybank.chatadapter.netty.server.dataproviders;

import com.buddybank.chatadapter.netty.enums.CustomEvents;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IDPCallback {
	/**
	 * This callback will be invoked if something goes wrong querying the
	 * Genesys backend. Two kinds of error can be generated:
	 * <ul>
	 * <li>either an instance of {@link Throwable}, if chat-adapter related
	 * error</li>
	 * <li>or an instance of {@link ObjectNode}, if Genesys-related error</li>
	 * </ul>
	 * 
	 * @param error
	 *            The error happened querying the Genesys backend
	 * @see ObjectNode
	 * @see Throwable
	 */
	void onError(Object error);

	/**
	 * This callback will be invoked when, after querying Genesys, new chat
	 * messages to be sent the client have been returned.
	 * 
	 * @param msg
	 *            The chat message(s) to be sent the client
	 * @see ObjectNode
	 */
	void onDataAvailable(ObjectNode msg);

	/**
	 * This callback will be invoked when a custom event has been fired by the
	 * thread which is querying Genesys. {@link CustomEvents} enumeration
	 * contains all the available events can be fired. Any new custom event has
	 * to be specified inside this enumeration.
	 * 
	 * @param evt
	 *            The custom event to be fired.
	 * @see CustomEvents
	 */
	void fireCustomEvent(CustomEvents evt);
}
