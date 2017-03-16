/**
 * 
 */
package com.buddybank.chatadapter.netty.server.handlers.codecs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.Serializer;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.chatadapter.netty.enums.GenesysEventType;
import com.buddybank.chatadapter.netty.enums.GenesysNoticeType;
import com.buddybank.chatadapter.netty.server.utils.Utils;
import com.buddybank.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author c309844
 *
 */
public class ByteToJsonChatMessageDecoder extends MessageToMessageDecoder<ByteBuf> implements Serializer<JsonNode> {
	private static final Logger LOG = LoggerFactory.getLogger(ByteToJsonChatMessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		JsonNode jsonMessage;
		int readableBytes = in.readableBytes();
		if (in.hasArray()) {
			jsonMessage = Utils.getMapper().readTree(in.array());
		} else {
			byte[] array = new byte[readableBytes];
			in.getBytes(in.readerIndex(), array);
			jsonMessage = Utils.getMapper().readTree(array);
		}
		// Msg
		ArrayNode messagesList = Utils.getJson()
				.getMandatoryArrayNode(Utils.getJson().getNodeValueOrFail(jsonMessage, "body"), "Msg");
		jsonMessage = this.toJson(jsonMessage);

		// for each outputMsg inside the array, create a new one including
		// preceding fields
		for (JsonNode jsonNode : messagesList) {
			JsonNode msg = ((ObjectNode) jsonMessage).setAll(this.validateMessage(jsonNode));
			LOG.info("Client {} is sending message: {}", ctx.channel().remoteAddress(), msg);
			out.add(msg);
		}
	}

	@Override
	public ObjectNode toJson(JsonNode entity, String... params) throws JsonProcessingException {
		ObjectNode responseObj = Utils.getMapper().createObjectNode();
		// header
		JsonNode node;
		try {
			node = Utils.getJson().getNodeValueOrFail(entity, "header");
			// key
			responseObj.put("key", Utils.getJson().getMandatoryStringValue(node, "key"));
			// username
			responseObj.put("username", Utils.getJson().getMandatoryStringValue(node, "username"));
			// xBBbackend
			responseObj.put("xBBbackend", Utils.getJson().getIntValueOrFail(node, "xBBbackend"));
			// body
			node = Utils.getJson().getNodeValueOrFail(entity, "body");
			// lastPosition (to be renamed in "fromPosition" for Genesys)
			responseObj.put("fromPosition", Utils.getJson().getMandatoryStringValue(node, "lastPosition"));
			// set action to UPDATE
			responseObj.put("action", "update");
		} catch (ClientErrorUnprocessableEntityException e) {
			e.printStackTrace();
		}
		return responseObj;
	}

	// method to validate the incoming outputMsg
	private ObjectNode validateMessage(JsonNode entity) throws Exception {
		ObjectNode responseObj = Utils.getMapper().createObjectNode();
		// id
		responseObj.put("id", Utils.getJson().getMandatoryStringValue(entity, "id"));
		// message
		String value = Utils.getJson().getStringValue(entity, "message");
		if (value == null) {
			responseObj.put("message", "");
		} else {
			responseObj.put("message", value);
		}
		// eventType
		value = Utils.getJson().getMandatoryStringValue(entity, "eventType");
		responseObj.put("eventType", value);
		switch (GenesysEventType.fromValue(value)) {
		case CONNECT:
		case MESSAGE:
			// noticeType
			value = Utils.getJson().getStringValue(entity, "noticeType");
			responseObj.put("noticeType", value == null ? "10" : value);
			break;
		case NOTICE:
			// noticeType
			responseObj.put("noticeType", Utils.getJson().getMandatoryStringValue(entity, "noticeType"));
			break;
		default:
			throw new ClientErrorUnprocessableEntityException("eventType",
					StringUtils.joinCollection(", ", GenesysNoticeType.values()), value);
		}

		return responseObj;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
