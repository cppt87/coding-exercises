/**
 * 
 */
package com.buddybank.chatadapter.netty.server;

import java.util.concurrent.ConcurrentMap;

import com.buddybank.chatadapter.netty.server.initializers.SecureChatServerInitializer;

import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

/**
 * @author c309844
 *
 */
public class SecureChatSocketsServer extends AbsChatServer {
	final SslContext sslContext;

	/**
	 * @param address
	 */
	public SecureChatSocketsServer(SslContext sslContext) {
		this.sslContext = sslContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buddybank.chatadapter.netty.server.AbsChatServer#createInitializer(io.netty.
	 * channel.group.ChannelGroup)
	 */
	@Override
	protected ChannelInitializer<SocketChannel> createInitializer(ConcurrentMap<String, ChannelId> sessions) {
		return new SecureChatServerInitializer(sessions, this.sslContext);
	}
}
