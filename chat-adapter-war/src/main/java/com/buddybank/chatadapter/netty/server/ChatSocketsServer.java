/**
 * 
 */
package com.buddybank.chatadapter.netty.server;

import java.util.concurrent.ConcurrentMap;

import com.buddybank.chatadapter.netty.server.initializers.ChatServerInitializer;

import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author c309844
 *
 */
public class ChatSocketsServer extends AbsChatServer {

	@Override
	protected ChannelInitializer<SocketChannel> createInitializer(ConcurrentMap<String, ChannelId> sessions) {
		return new ChatServerInitializer(sessions);
	}
}
