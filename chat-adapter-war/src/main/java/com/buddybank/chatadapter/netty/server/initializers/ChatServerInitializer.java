/**
 * 
 */
package com.buddybank.chatadapter.netty.server.initializers;

import java.util.concurrent.ConcurrentMap;

import com.buddybank.chatadapter.netty.server.handlers.ChatServiceHandler;
import com.buddybank.chatadapter.netty.server.handlers.MyLoggingHandler;
import com.buddybank.chatadapter.netty.server.handlers.PingAndJsonObjectDecoder;
import com.buddybank.chatadapter.netty.server.handlers.codecs.CombinedByteJsonChatMessageCodec;
import com.buddybank.chatadapter.netty.server.utils.Utils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * @author c309844
 *
 */
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
	// sharable instance of workers group
	private final DefaultEventExecutorGroup executorGroupInstance = new DefaultEventExecutorGroup(
			Utils.DEFAULT_EXECUTOR_THREADS_NUMBER);
	// sharable, thus create only one instance to be used among all channels
	private static final ChannelHandler LOGGER_INSTANCE = new MyLoggingHandler();
	// server sessions
	private final ConcurrentMap<String, ChannelId> sessions;

	/**
	 * 
	 */
	public ChatServerInitializer(ConcurrentMap<String, ChannelId> sessions) {
		this.sessions = sessions;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("loggingHandler", LOGGER_INSTANCE)
				.addLast("idleStateHandler", new IdleStateHandler(0, 0, 50))
				.addLast("byteToJsonDecoder", new PingAndJsonObjectDecoder(Utils.MAX_FRAME_SIZE, false))
				.addLast("jsonNodeCodec", new CombinedByteJsonChatMessageCodec())
				// add the "BL handler" with a proper executor threads pool
				.addLast(this.executorGroupInstance, "chatServiceHandler", new ChatServiceHandler(this.sessions));
	}
}
