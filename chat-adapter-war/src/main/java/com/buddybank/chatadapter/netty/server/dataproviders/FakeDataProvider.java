package com.buddybank.chatadapter.netty.server.dataproviders;

import com.buddybank.api.utils.JsonNodeUtils;
import com.buddybank.chatadapter.netty.server.utils.ChatSession;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * @author c309844
 *
 */
public class FakeDataProvider extends AbsDataProvider {

	public FakeDataProvider(IDPCallback callback, ChatSession session) {
		super(callback, session);
	}

	@Override
	public void run() {
		try {
			// elaboro il messaggio originario
			Thread.sleep(2000);
			ObjectNode nextMsg = this.formatGenesysInputMsg();
			LOG.debug("Message to be sent Genesys: {}", nextMsg);
			// now nextMesg contains http response from Genesys: let's evaluate it
			this.evaluateAndSendResponse((ObjectNode) JsonNodeUtils.loadRawListFromResource("messages.json")
					.get(ThreadLocalRandom.current().nextInt(4)));
		} catch (Exception e) {
			this.callback.onError(e);

		}
	}
}
