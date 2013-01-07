package controllers.player;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;
import controllers.WebSocketActor;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;
import controllers.message.TriggerReload;

public class PlayerActor extends WebSocketActor {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(PlayerActor.class);

	@Override
	public String handleMessage(String message) {
		JsonNode json = Json.parse(message);
		DataContainer dataContainer = Json.fromJson(json, DataContainer.class);
		DataContainer response = dataContainer.execute();
		if (response instanceof SimpleResponse) {
			if (((SimpleResponse) response).isSuccessful()) {
				getContext().self().tell(new TriggerReload());
			}
		}
		if (response != null) {
			return Json.stringify(Json.toJson(response));
		}
		return null;
	}

	@Override
	public void handleTriggerReload(TriggerReload trigger) {
		long start = System.currentTimeMillis();
		try {
			Iterator<WebSocketContext> iterator = getWebSockets();
			while (iterator.hasNext()) {
				WebSocketContext ctx = iterator.next();
				ctx.getWebSocketOut().write(
						Json.stringify(Json.toJson(trigger)));
			}
		} finally {
			LOGGER.debug("HandleTriggerReload took {} [ms]",
					System.currentTimeMillis() - start);
		}

	}
}
