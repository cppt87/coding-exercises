/**
 * 
 */
package com.buddybank.chatadapter.netty.server;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.channel.ChannelFuture;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @author c309844
 *
 */
public class NettyServerMainThread implements Runnable {
	private static final boolean SSL = System.getProperty("ssl") != null;
	private AbsChatServer nettyServer;

	@Override
	public void run() {
		System.out.println("Invocato costruttore");
		if (SSL) {
			SelfSignedCertificate ssc;
			SslContext sslCtx;
			try {
				ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
				this.nettyServer = new SecureChatSocketsServer(sslCtx);
			} catch (CertificateException e) {
				e.printStackTrace();
				this.nettyServer = new ChatSocketsServer();
			} catch (SSLException e) {
				e.printStackTrace();
				this.nettyServer = new ChatSocketsServer();
			}
		} else {
			this.nettyServer = new ChatSocketsServer();
		}

		ChannelFuture future = this.nettyServer.start();
		future.channel().closeFuture().syncUninterruptibly();
	}

	public void stop() throws InterruptedException {
		this.nettyServer.destroy();
	}
}
