/**
 * 
 */
package com.buddybank.chatadapter.netty.server.handlers.codecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.api.Deserializer;
import com.buddybank.chatadapter.netty.enums.ChatBackendType;
import com.buddybank.chatadapter.netty.enums.GenesysErrorsList;
import com.buddybank.chatadapter.netty.server.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author c309844
 *
 */
public class JsonChatMessageToByteEncoder extends MessageToByteEncoder<ObjectNode> implements Deserializer<byte[]> {
	private static final Logger LOG = LoggerFactory.getLogger(JsonChatMessageToByteEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, ObjectNode msg, ByteBuf out) throws Exception {
		out.writeBytes(this.toObject(msg));
	}

	@Override
	public byte[] toObject(JsonNode node, String... params) {
		// header
		ObjectNode header = Utils.getMapper().createObjectNode();
		// key
		header.put("key", ((ObjectNode) node).remove("key"));
		// username
		header.put("username", ((ObjectNode) node).remove("username"));
		// xBBbackend
		header.put("xBBbackend",
				ChatBackendType.valueOf(((ObjectNode) node).remove("xBBbackend").textValue()).ordinal());
		// set the header
		((ObjectNode) node).put("header", header);
		// body
		ObjectNode body = Utils.getMapper().createObjectNode();
		// Msg
		body.put("Msg", ((ObjectNode) node).remove("Msg"));
		// lastPosition
		JsonNode value = ((ObjectNode) node).remove("lastPosition");
		if (value != null) {
			body.put("lastPosition", value.intValue());
		}
		// error
		value = ((ObjectNode) node).remove("error");
		if (value != null && value.intValue() != 0) {
			body.put("error", GenesysErrorsList.fromValue(value.intValue()).ordinal());
			// errorDesc
			value = ((ObjectNode) node).remove("errorDesc");
			body.put("errorDesc", value.textValue());
		} else {
			// simply remove errorDesc
			((ObjectNode) node).remove("errorDesc");
		}
		// messagesStatus
		value = ((ObjectNode) node).remove("messagesStatus");
		if (value != null) {
			body.put("messagesStatus", value.intValue());
		}
		// set the whole node
		((ObjectNode) node).put("body", body);
		// message logging
		LOG.info("Genesys is sending message: {}", node);
		return node.toString().getBytes();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
