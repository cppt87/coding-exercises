package com.buddybank.chatadapter.netty.server.handlers;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.api.exceptions.ClientErrorBadRequestException;
import com.buddybank.api.exceptions.ClientErrorUnauthorizedException;
import com.buddybank.api.exceptions.ClientErrorUnprocessableEntityException;
import com.buddybank.api.exceptions.ServerErrorInternalException;
import com.buddybank.api.exceptions.ServerErrorServiceUnavailableException;
import com.buddybank.chatadapter.netty.enums.ChatBackendType;
import com.buddybank.chatadapter.netty.enums.ChatClientStates;
import com.buddybank.chatadapter.netty.enums.CustomEvents;
import com.buddybank.chatadapter.netty.enums.GenesysEventType;
import com.buddybank.chatadapter.netty.server.dataproviders.FakeDataProvider;
import com.buddybank.chatadapter.netty.server.dataproviders.HTTPDataProvider;
import com.buddybank.chatadapter.netty.server.dataproviders.IDPCallback;
import com.buddybank.chatadapter.netty.server.dataproviders.IDataProvider;
import com.buddybank.chatadapter.netty.server.utils.ChatSession;
import com.buddybank.chatadapter.netty.server.utils.Utils;
import com.buddybank.utils.StringUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;

/**
 * @author c309844
 *
 */
public class ChatServiceHandler extends SimpleChannelInboundHandler<ObjectNode> {
	private static final Logger LOG = LoggerFactory.getLogger(ChatServiceHandler.class);
	private Future<?> future;
	private final ConcurrentMap<String, ChannelId> activeSessions;
	private IDataProvider<ObjectNode> dataProvider;
	private ChatSession session;
	// number of consequent errors
	private int numError;
	private ChannelFutureListener listener;

	public ChatServiceHandler(ConcurrentMap<String, ChannelId> sessions) {
		// get the reference to the server activeSessions list
		this.activeSessions = sessions;
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// create temporary session, bound to current channelId
		this.session = new ChatSession(ctx.channel().id());
		// accept only clients coming from "DISCONNECTED" state
		switch (this.session.getState()) {
		case DISCONNECTED:
			// set client status to "CONNECTING"
			this.session.setState(ChatClientStates.CONNECTING);
			/*
			 * wait 10 seconds for login message, after that channel will be
			 * closed
			 */
			this.future = ctx.executor().schedule(new Runnable() {
				@Override
				public void run() {
					exceptionCaught(ctx,
							new ClientErrorUnauthorizedException(new Throwable(MessageFormat.format(
									"Client {0} took more than 10 seconds to verify credentials. Connection refused",
									ctx.channel().remoteAddress().toString()))));
				}
			}, 10, TimeUnit.SECONDS);
			break;
		default:
			exceptionCaught(ctx, new ClientErrorUnauthorizedException());
		}
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, ObjectNode msg) throws Exception {
		// check "eventType"
		String value = Utils.getJson().getMandatoryStringValue(msg, "eventType");
		// enumeration validation
		switch (this.session.getState()) {
		// if "login" message, verify credentials
		case CONNECTING:
			try {
				// enumeration validation
				switch (GenesysEventType.fromValue(value)) {
				// if "login" message, verify credentials
				case CONNECT:
					// reset the timer
					this.future.cancel(true);
					// set up key and username
					String sessionKey = Utils.getJson().getMandatoryStringValue(msg, "key");
					this.session.getHeader().put("key", sessionKey);
					this.session.getHeader().put("username", Utils.getJson().getMandatoryStringValue(msg, "username"));
					sessionKey += this.session.getHeader().get("username");
					// check if a session already exists
					if (this.activeSessions.containsKey(sessionKey)) {
						// if exists, FRAUD ATTEMPT!!!
						// raise exception and release all resources
						this.exceptionCaught(ctx,
								new ClientErrorUnauthorizedException(new Throwable("FRAUD ATTEMPT!!!")));
					} else {
						// create global channel listener
						listener = new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								if (future.cause() != null && ctx.channel().isActive()) {
									exceptionCaught(ctx, new ServerErrorInternalException(future.cause()));
								}
							}
						};
						// join current session the list of active ones
						this.activeSessions.put(sessionKey, this.session.getChannelId());
						// switch state to LOGIN_MESSAGE_RECEIVED
						this.session.setState(ChatClientStates.LOGIN_MSG_RECEIVED);
						// set backend type
						this.session.getHeader().put("xBBbackend", ChatBackendType
								.fromValue(Utils.getJson().getIntValueOrFail(msg, "xBBbackend")).toString());
						// set login message as the current one
						this.session.setCurrentMessage(msg);
						// credential validations
						this.future = ctx.executor().scheduleAtFixedRate(this.getDataProvider(new IDPCallback() {

							@Override
							public void onDataAvailable(ObjectNode obj) {
								// call successfully submitted
								if (session.getState() == ChatClientStates.CONNECTED_SOCK_OPN) {
									// I have been able to write over channel:
									// reset the counter
									numError = 0;
									if (ctx.channel().isWritable())
										ctx.writeAndFlush(obj).addListener(listener);
									else {
										LOG.warn("Enqueuing message {}", obj);
										session.getOutQueue().offer(obj);
									}
								} else {
									// trying to stop poller again
									LOG.warn("Emptying queue...");
									shutdownGracefully();
								}
							}

							@Override
							public void onError(Object error) {
								if (error instanceof Throwable) {
									// rise exception
									exceptionCaught(ctx, new ServerErrorInternalException((Throwable) error));
								} else {
									try {
										// write error message to channel if
										// open
										if (session.getState() == ChatClientStates.CONNECTED_SOCK_OPN
												&& ctx.channel().isWritable()) {
											ctx.writeAndFlush(error).addListener(listener);
										}
										// rise exception
										exceptionCaught(ctx, new ServerErrorServiceUnavailableException("Genesys",
												Utils.getJson().getStringValue((ObjectNode) error, "error"),
												Utils.getJson().getStringValue((ObjectNode) error, "errorDesc")));
									} catch (Exception e) {
										// rise exception
										exceptionCaught(ctx, new ServerErrorInternalException(e));
									}
								}
							}

							@Override
							public void fireCustomEvent(CustomEvents evt) {
								LOG.info("Fired custom event {} with status {}", evt, session.getState());
								if (session.getState() == ChatClientStates.LOGIN_MSG_RECEIVED
										&& evt == CustomEvents.LOGIN_SUCCESSFULLY) {
									// set client status to "CONNECTED_SOCK_OPN"
									session.setState(ChatClientStates.CONNECTED_SOCK_OPN);
								} else if (session.getState() == ChatClientStates.CONNECTED_SOCK_OPN
										&& evt == CustomEvents.CHAT_CLOSE) {
									// close everything
									numError = 2;
									exceptionCaught(ctx, null);
								} else {
									// increment errors count so that channel
									// will be destroyed
									numError = 2;
									exceptionCaught(ctx,
											new ServerErrorInternalException(new Throwable(MessageFormat.format(
													"Unexpected [{0}] event fired with channel status [{1}]",
													evt.toString(), session.getState().toString()))));
								}
							}
						}), 0, 1, TimeUnit.SECONDS);
					}
					break;
				default:
					exceptionCaught(ctx, new ClientErrorUnauthorizedException());
				}
			} catch (IllegalArgumentException e) {
				exceptionCaught(ctx, new ClientErrorUnprocessableEntityException("eventType",
						StringUtils.joinCollection(", ", GenesysEventType.values()), value));
			}
			break;
		case LOGIN_MSG_RECEIVED:
		case CONNECTED_SOCK_OPN:
			try {
				// enumeration validation
				switch (GenesysEventType.fromValue(value)) {
				// only messages
				case MESSAGE:
				case NOTICE:
					// enqueue the message
					this.session.getInQueue().offer(msg);
					break;
				default:
					exceptionCaught(ctx,
							new ClientErrorBadRequestException("Only values 1 or 3 for parameter [eventType] allowed"));
				}
			} catch (IllegalArgumentException e) {
				exceptionCaught(ctx, new ClientErrorUnprocessableEntityException("eventType",
						StringUtils.joinCollection(", ", GenesysEventType.values()), value));
			}
			break;
		default:
			exceptionCaught(ctx, new ClientErrorUnauthorizedException());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (this.session != null) {
			// set client status to "CONNECTED_SOCK_CLS"
			this.session.setState(ChatClientStates.CONNECTED_SOCK_CLS);
			// try to stop poller
			this.shutdownGracefully();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// close socket after three consequent errors, or immediately after
		// login error
		if (++this.numError > 2 || this.session.getState().ordinal() < ChatClientStates.CONNECTED_SOCK_OPN.ordinal()) {
			// empty queues
			this.session.getInQueue().clear();
			this.session.getOutQueue().clear();
			// close the channel
			if (ctx.channel().isOpen()) {
				// if isOpen, shutdownGracefully called when channelInactive is
				// triggered
				ctx.close();
			} else {
				// otherwise, I must call it by myself
				this.shutdownGracefully();
			}
		}
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		this.writeIfPossible(ctx.channel());
	}

	// release all the resources, when appropriate and possible
	private void shutdownGracefully() {
		String sessionKey = this.session.getHeader().get("key");
		// empty the queue sending remaining messages
		if (sessionKey != null && this.activeSessions.get(sessionKey += this.session.getHeader().get("username"))
				.equals(this.session.getChannelId()) && this.session.getInQueue().isEmpty()) {
			// remove session from list of active ones
			String id = this.activeSessions.remove(sessionKey).asShortText();
			LOG.info("Session {} - {} destroyed", sessionKey, id);
			// invalidate session
			this.session = null;
			// trying to stop poller again
			if (this.future != null && this.future.isCancellable()) {
				// invalidate session
				this.future.cancel(true);
				LOG.info("{} channel's poller thread stopped", id);
			}
		}
	}

	// select the correct instance of data provider
	private IDataProvider<ObjectNode> getDataProvider(IDPCallback callback) throws Exception {
		if (this.dataProvider != null) {
			return this.dataProvider;
		} else if (ChatBackendType.FAKE.toString().equalsIgnoreCase(this.session.getHeader().get("xBBbackend"))) {
			return (this.dataProvider = new FakeDataProvider(callback, this.session));
		} else {
			return (this.dataProvider = new HTTPDataProvider(callback, this.session));
		}
	}

	// method to write on channel only if it is writable
	private void writeIfPossible(Channel channel) {
		// check if the out queue is not empty, and if the channel is writable
		while (!this.session.getOutQueue().isEmpty() && channel.isWritable()) {
			LOG.warn("Emptying out queue...");
			// write queued messages over the channel
			channel.writeAndFlush(this.session.getOutQueue().poll()).addListener(listener);
		}
	}
}
