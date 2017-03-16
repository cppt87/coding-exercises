package com.buddybank.chatadapter.netty;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buddybank.chatadapter.netty.server.NettyServerMainThread;
import com.buddybank.chatadapter.netty.server.utils.NettyMainThreadShutdownHook;

/**
 * Servlet implementation class ChatServerBootstrapServlet
 */
public class ChatServerBootstrapServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ThreadPoolExecutor bootstrapThread;
	private NettyServerMainThread nettyMainThread;
	private NettyMainThreadShutdownHook nettyMainThreadShutdownHook;

	/**
	 * 
	 */
	public ChatServerBootstrapServlet() {
		this.nettyMainThread = new NettyServerMainThread();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init() {
		try {
			this.startStopServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("Invocata init");

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			System.out.println("GET /chatServerSwitch fired!");
			this.startStopServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startStopServer() throws InterruptedException, ExecutionException {
		if (this.bootstrapThread != null && this.bootstrapThread.getActiveCount() == 1) {
			this.releaseAllResources();
		} else {
			this.bootstrapThread = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>());
			this.bootstrapThread.execute(this.nettyMainThread);
			this.nettyMainThreadShutdownHook = new NettyMainThreadShutdownHook(this.bootstrapThread,
					this.nettyMainThread);
			Runtime.getRuntime().addShutdownHook(this.nettyMainThreadShutdownHook);
			System.out.println("Added ChatServlet shoutdown hook!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		if (this.bootstrapThread != null && this.bootstrapThread.getActiveCount() == 1) {
			System.out.println("Invocata destroy");
			try {
				this.releaseAllResources();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.destroy();
	}

	private void releaseAllResources() throws InterruptedException {
		this.nettyMainThread.stop();
		this.bootstrapThread.shutdownNow();
		Runtime.getRuntime().removeShutdownHook(this.nettyMainThreadShutdownHook);
		System.out.println("All the resources released!");
	}
}
