/**
 * 
 */
package com.buddybank.chatadapter.netty.server;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.chatadapter.netty.server.utils.Utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author c309844
 *
 */
public abstract class AbsChatServer {
	private static final Logger LOG = LoggerFactory.getLogger(AbsChatServer.class);
	// List of channels
	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	// List of active sessions
	private final ConcurrentMap<String, ChannelId> sessions = new ConcurrentHashMap<String, ChannelId>();
	// Pooled threads which will work with connection handling
	private EventLoopGroup bossGroup;
	// Pooled threads which will manage each client/server channel
	private EventLoopGroup workerGroup;
	private Channel channel;

	// select the right initializer according to SSL availability
	abstract protected ChannelInitializer<SocketChannel> createInitializer(ConcurrentMap<String, ChannelId> sessions);

	// start the server
	public ChannelFuture start() {
		this.bossGroup = this.selectEventLoopGroup(1);
		this.workerGroup = this.selectEventLoopGroup(Utils.DEFAULT_THREADS_NUMBER);
		ServerBootstrap b = new ServerBootstrap(); // (2)
		b.group(bossGroup, workerGroup).channel(this.selectServerSocketChannel()) // (3)
				.handler(new LoggingHandler(LogLevel.INFO)).childHandler(this.createInitializer(this.sessions))
				.option(ChannelOption.SO_BACKLOG, 128) // (5)
				.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
						new WriteBufferWaterMark(Utils.BUFFER_LOW_WATERMARK, Utils.BUFFER_HIGH_WATERMARK));
		// Start the server...
		ChannelFuture future = null;
		try {
			Properties props = Utils.getProperties();
			// ... looping over all available ports
			for (int i = 0; future == null && i < props.size(); i++) {
				int port = Integer.parseInt(props.getProperty("port_" + i));
				try {
					future = b.bind(port).syncUninterruptibly();
				} catch (Exception e) {
					if (i != props.size()) {
						LOG.warn("Port {} is busy. Trying to bind next one...", port);
					} else {
						e.printStackTrace();
						LOG.error("[FATAL] Last available port {} is busy. Chat adapter not started.", port);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return future;
	}

	// destroy the server instance
	public void destroy() throws InterruptedException {
		if (this.channel != null) {
			this.channel.close();
		}
		this.channelGroup.close();
		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();
		LOG.info("Bye bye Netty! ;-)");
	}

	/*
	 * select the most performing event loop group according to the underlying
	 * OS: - DEFAULT nThreads = MAX(1, available_processors * 2);
	 */
	private EventLoopGroup selectEventLoopGroup(int nThreads) {
		return (Utils.OS != null && Utils.OS.startsWith("Linux") ? new EpollEventLoopGroup(nThreads)
				: new NioEventLoopGroup(nThreads));
	}

	// select the most performing socket channel according to the underlying OS
	private Class<? extends ServerChannel> selectServerSocketChannel() {
		return (Utils.OS != null && Utils.OS.startsWith("Linux") ? EpollServerSocketChannel.class
				: NioServerSocketChannel.class);
	}
}
