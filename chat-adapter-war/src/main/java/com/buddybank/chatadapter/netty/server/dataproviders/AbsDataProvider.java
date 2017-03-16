package com.buddybank.chatadapter.netty.server.dataproviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.chatadapter.netty.enums.ChatUserType;
import com.buddybank.chatadapter.netty.enums.CustomEvents;
import com.buddybank.chatadapter.netty.enums.GenesysErrorsList;
import com.buddybank.chatadapter.netty.enums.GenesysEventType;
import com.buddybank.chatadapter.netty.enums.GenesysNoticeType;
import com.buddybank.chatadapter.netty.server.utils.ChatSession;
import com.buddybank.chatadapter.netty.server.utils.Utils;
import com.buddybank.utils.StringUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author c309844
 *
 */
public abstract class AbsDataProvider implements IDataProvider<ObjectNode> {
	protected static final Logger LOG = LoggerFactory.getLogger(AbsDataProvider.class);
	protected final IDPCallback callback;
	protected int lastPosition;
	protected ChatSession session;

	/**
	 * 
	 */
	protected AbsDataProvider(IDPCallback callback, ChatSession session) {
		this.session = session;
		this.callback = callback;
		this.lastPosition = 1;
	}

	// method to format body for Genesys call
	protected ObjectNode formatGenesysInputMsg() throws Exception {
		// if null, check the queue
		if (this.session.getCurrentMessage() == null) {
			// create a copy of the next queued message
			this.session.setCurrentMessage(this.session.getInQueue().peek());
		}

		if (this.session.getCurrentMessage() != null) {
			// update lastPosition with the maximum between the internal, and
			// the last sent by app
			this.lastPosition = Math.max(
					Utils.getJson().getIntValueOrFail(this.session.getCurrentMessage(), "fromPosition"),
					this.lastPosition);
			// update lastPosition with the true last one
			this.session.getCurrentMessage().put("fromPosition", String.valueOf(this.lastPosition));
			// remove internal parameters
			return this.cleanInputMsg(
					(ObjectNode) Utils.getMapper().createObjectNode().setAll(this.session.getCurrentMessage()));
		} else { // null input. Just create a poll message
			this.session.setCurrentMessage(Utils.getMapper().createObjectNode());
			// action: must be valued "update"
			return (this.session.getCurrentMessage().put("action", "update")
					// fromPosition. Should be not null if login message
					// received
					.put("fromPosition", String.valueOf(this.lastPosition))
					// key. Should be not null if login message received
					.put("key", this.session.getHeader().get("key"))
					// username. Should be not null if login message received
					.put("username", this.session.getHeader().get("username"))
					// message. For polling message must be valued ""
					.put("message", "")
					// noticeType. For polling message must be valued 0
					.put("noticeType", GenesysNoticeType.NONE.ordinal()));
		}
	}

	// this method evaluate Genesys response, formatting and sending it to the
	// channel
	protected void evaluateAndSendResponse(ObjectNode response) throws Exception {
		// check request result
		// if no error
		if (GenesysErrorsList
				.fromValue(Utils.getJson().getIntValueOrFail(response, "error")) == GenesysErrorsList.NO_ERROR) {
			// update internal lastPosition
			this.lastPosition = Utils.getJson().getIntValueOrFail(response, "lastPosition");
			// check the original event type
			String value = Utils.getJson().getStringValue(this.session.getCurrentMessage(), "eventType");
			// no confirmation for polling
			if (value != null
					&& !Utils.getJson().getStringValue(this.session.getCurrentMessage(), "noticeType").matches("2|3")) {
				if (GenesysEventType.fromValue(value) == GenesysEventType.CONNECT) {
					// login successfully!
					this.callback.fireCustomEvent(CustomEvents.LOGIN_SUCCESSFULLY);
				}
				// sending confirmation, if not typing started or typing ended
				this.generateSendingConfirmation(response);
			}
			// no error. I can make "null" original message:
			// 1) removing it from queue (if present)
			this.session.getInQueue().poll();
			// delete from array my messages and notice,
			// and send message
			this.cleanAndSendResponse(response);
		} else { // in case of error coming from Genesys...
					// header adding
			response.setAll(Utils.getJson().mapToObject(this.session.getHeader()));
			this.callback.onError(this.cleanOutputMsg(response));
		}
		// 2) setting it "null"
		this.session.setCurrentMessage(null);
	}

	// method to remove "internal" parameters
	private ObjectNode cleanInputMsg(ObjectNode msg) {
		// remove xBBbackend
		msg.remove("xBBbackend");
		// remove id
		msg.remove("id");
		// remove eventType
		msg.remove("eventType");
		// remove timestamp (if present)
		msg.remove("timestamp");
		// remove messagesStatus (if present)
		msg.remove("messagesStatus");
		/*
		 * add here all other parameters to be removed...
		 * 
		 */
		return msg;
	}

	// method for "internal" parameters and useless messages removing
	private void cleanAndSendResponse(ObjectNode response) throws Exception {
		boolean fireCloseChannel = false;
		// clean array of messages (if present)
		ArrayNode msgs = Utils.getJson().getArrayNode(response, "Msg");
		for (int i = 0; i < msgs.size();) {
			String value = Utils.getJson().getMandatoryStringValue(msgs.get(i), "eventType");
			switch (GenesysEventType.fromValue(value)) {
			case CONNECT:
				// agent available!
				// this.callback.fireCustomEvent(CustomEvents.AGENT_AVAILABLE);
				i++;
				break;
			case MESSAGE:
			case NOTICE:
				if ((GenesysNoticeType
						.fromValue(Utils.getJson().getMandatoryStringValue(msgs.get(i),
								"noticeType")) == GenesysNoticeType.PING
						&& !Utils.getJson().getStringValue(msgs.get(i), "textw").matches("\\w+"))
						|| ChatUserType.fromValue(Utils.getJson().getMandatoryStringValue(msgs.get(i),
								"userType")) == ChatUserType.CLIENT) {
					// remove if ping message or a message of mine
					msgs.remove(i);
				} else {
					i++;
				}
				break;
			case ABANDON:
				// chat closing!
				fireCloseChannel = ChatUserType.fromValue(
						Utils.getJson().getMandatoryStringValue(msgs.get(i), "userType")) == ChatUserType.CLIENT;
				i++;
				break;
			default:
				this.callback.onError(new ClientErrorUnprocessableEntityException("eventType",
						StringUtils.joinCollection(", ", GenesysEventType.values()), value));
			}
		}
		// if array not null
		if (msgs.size() > 0) {
			// remove ProtocolVersion (if present)
			this.cleanOutputMsg(response);
			// header adding
			response.setAll(Utils.getJson().mapToObject(this.session.getHeader()));
			// message sending
			this.callback.onDataAvailable(response);
		}
		// if chat has to be closed
		if (fireCloseChannel) {
			this.callback.fireCustomEvent(CustomEvents.CHAT_CLOSE);
		}
	}

	private void generateSendingConfirmation(ObjectNode response) throws Exception {
		ArrayNode msgs = Utils.getJson().getArrayNode(response, "Msg");
		if (msgs == null) {
			msgs = Utils.getMapper().createArrayNode();
		}
		// create confirmation message
		ObjectNode confirm = Utils.getMapper().createObjectNode();
		// id
		confirm.put("id", Utils.getJson().getMandatoryStringValue(this.session.getCurrentMessage(), "id"));
		// noticeType 12 = MSG_SENT
		confirm.put("noticeType", 12);
		// eventType 3 = SEE_NOTICE
		confirm.put("eventType", GenesysEventType.NOTICE.ordinal());
		// userType 3 = EXTERNAL
		confirm.put("userType", ChatUserType.EXTERNAL.ordinal());
		// add msg to array
		msgs.add(confirm);
		// substitute old array with the new one
		response.put("Msg", msgs);
		// add message status SENT = 2
		response.put("messagesStatus", 2);
	}

	// add headers and fire onDataAvailable callback
	private ObjectNode cleanOutputMsg(ObjectNode msg) {
		// remove ProtocolVersion (if present)
		msg.remove("ProtocolVersion");
		/*
		 * 
		 * add here all others parameters to be removed...
		 * 
		 * 
		 */
		return msg;
	}
}
