package com.buddybank.chatadapter.netty.server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.buddybank.chatadapter.netty.enums.ChatClientStates;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.channel.ChannelId;

public class ChatSession {
	// client's queue for input messages
	private final Queue<ObjectNode> inQueue;
	// client's queue for output messages
	private final Queue<ObjectNode> outQueue;
	// current state of the client
	private ChatClientStates state;
	// current channelId
	private final ChannelId channelId;
	// current client key, username and backend type
	private final Map<String, String> header;
	// current message to be managed
	private ObjectNode currentMessage;

	public ChatSession(ChannelId channelId) {
		// set channelId
		this.channelId = channelId;
		// set initial client state
		this.state = ChatClientStates.DISCONNECTED;
		// create header map
		this.header = new HashMap<String, String>(3);
		// create new queues
		this.inQueue = new ConcurrentLinkedQueue<ObjectNode>();
		this.outQueue = new ConcurrentLinkedQueue<ObjectNode>();
	}

	/**
	 * @return the inQueue
	 */
	public Queue<ObjectNode> getInQueue() {
		return inQueue;
	}

	/**
	 * @return the outQueue
	 */
	public Queue<ObjectNode> getOutQueue() {
		return outQueue;
	}

	/**
	 * @return the state
	 */
	public ChatClientStates getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(ChatClientStates status) {
		this.state = status;
	}

	/**
	 * @return the channelId
	 */
	public ChannelId getChannelId() {
		return channelId;
	}

	/**
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		return header;
	}

	/**
	 * @return the currentMessage
	 */
	public ObjectNode getCurrentMessage() {
		return currentMessage;
	}

	/**
	 * @param currentMessage the currentMessage to set
	 */
	public void setCurrentMessage(ObjectNode currentMessage) {
		this.currentMessage = currentMessage;
	}
}