package com.buddybank.chatadapter.netty.server.handlers;

import java.nio.CharBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

// This handler handles the IdleStateEvent triggered by IdleStateHandler, and incoming bytes
public class PingAndJsonObjectDecoder extends JsonObjectDecoder {
	private static final Logger LOG = LoggerFactory.getLogger(PingAndJsonObjectDecoder.class);
	private boolean checkWritability = false;

	/**
	 * @param maxObjectLength
	 * @param streamArrayElements
	 */
	public PingAndJsonObjectDecoder(int maxObjectLength, boolean streamArrayElements) {
		super(maxObjectLength, streamArrayElements);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.ALL_IDLE) {
				LOG.info("Client {} seems to be inactive. Ping sending...", ctx.channel().remoteAddress().toString());
			} else if (e.state() == IdleState.WRITER_IDLE) {
				LOG.info("It is from a while any message has not been written. Ping sending...",
						ctx.channel().remoteAddress().toString());
			} else if (e.state() == IdleState.READER_IDLE) {
				LOG.info("It is from a while any message has not been read. Ping sending ...",
						ctx.channel().remoteAddress().toString());
			}
			// write a "ping" message
			if (!(this.checkWritability = !ctx.channel().isWritable()))
				ctx.writeAndFlush(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap("ping"), CharsetUtil.UTF_8));
		}
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		if (this.checkWritability && ctx.channel().isWritable()) {
			ctx.writeAndFlush(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap("ping"), CharsetUtil.UTF_8));
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// check if incoming bytes are of "ping" kind:
		if ("ping".equalsIgnoreCase(in.toString(0, 4, CharsetUtil.UTF_8))) {
			LOG.info("Heartbeat from client {} received", ctx.channel().remoteAddress().toString());
			// clear the buffer in
			in.clear();
		} else {
			// otherwise, just behave as a JsonObjectDecoder
			super.decode(ctx, in, out);
		}
	}
}
