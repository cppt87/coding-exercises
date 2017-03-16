/**
 * 
 */
package com.buddybank.chatadapter.netty.server.handlers.codecs;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @author c309844
 *
 */
public class CombinedByteJsonChatMessageCodec
		extends CombinedChannelDuplexHandler<ByteToJsonChatMessageDecoder, JsonChatMessageToByteEncoder> {
	/**
	 * 
	 */
	public CombinedByteJsonChatMessageCodec() {
		super(new ByteToJsonChatMessageDecoder(), new JsonChatMessageToByteEncoder());
	}
}
