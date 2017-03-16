/**
 * 
 */
package com.buddybank.chatadapter.netty.server.handlers;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;

/**
 * @author c309844
 *
 */
@Sharable
public class MyLoggingHandler extends LoggingHandler {
	private static final InternalLogLevel internalLevel = LogLevel.INFO.toInternalLevel();

	/**
	 * @param level
	 */
	public MyLoggingHandler() {
		super(LogLevel.DEBUG);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.log(internalLevel, format(ctx, "REGISTERED"));
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.log(internalLevel, format(ctx, "UNREGISTERED"));
		ctx.fireChannelUnregistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.log(internalLevel, format(ctx, "ACTIVE"));
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.log(internalLevel, format(ctx, "INACTIVE"));
		ctx.fireChannelInactive();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(internalLevel, format(ctx, "EXCEPTION", cause), cause);
		ctx.fireExceptionCaught(cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.log(internalLevel, format(ctx, "USER_EVENT", evt));
		ctx.fireUserEventTriggered(evt);
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		logger.log(internalLevel, format(ctx, "BIND", localAddress));
		ctx.bind(localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		logger.log(internalLevel, format(ctx, "CONNECT", remoteAddress, localAddress));
		ctx.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.log(internalLevel, format(ctx, "DISCONNECT"));
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.log(internalLevel, format(ctx, "CLOSE"));
		ctx.close(promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		if (logger.isEnabled(internalLevel)) {
			logger.log(internalLevel, format(ctx, "DEREGISTER"));
			ctx.deregister(promise);
		}
	}
}
