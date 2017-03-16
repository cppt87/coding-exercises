/**
 * 
 */
package com.buddybank.chatadapter.netty.server.utils;

import java.util.concurrent.ThreadPoolExecutor;

import com.buddybank.chatadapter.netty.server.NettyServerMainThread;

/**
 * @author c309844
 *
 */
public class NettyMainThreadShutdownHook extends Thread {
	private final ThreadPoolExecutor nettyMainThread;
	private final NettyServerMainThread chatServerMainThread;

	public NettyMainThreadShutdownHook(ThreadPoolExecutor thread, NettyServerMainThread chatServerMainThread) {
		this.nettyMainThread = thread;
		this.chatServerMainThread = chatServerMainThread;
	}

	@Override
	public void run() {
		System.out.println("MyShutDown Thread started");
		try {
			// stop the Netty server
			this.chatServerMainThread.stop();
			// stop the thread used to get running Netty server
			this.nettyMainThread.shutdownNow();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
