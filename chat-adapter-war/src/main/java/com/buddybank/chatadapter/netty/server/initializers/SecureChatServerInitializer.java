/**
 * 
 */
package com.buddybank.chatadapter.netty.server.initializers;

import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * @author c309844
 *
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
	private final SslContext sslContext;

	/**
	 * 
	 */
	public SecureChatServerInitializer(ConcurrentMap<String, ChannelId> sessions, SslContext sslContext) {
		super(sessions);
		this.sslContext = sslContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		super.initChannel(ch);
		ch.pipeline().addFirst(new SslHandler(this.sslContext.newEngine(ch.alloc())));
	}
}
