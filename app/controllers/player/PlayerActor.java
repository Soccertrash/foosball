package controllers.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.Player;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.WebSocketActor;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;
import controllers.message.TriggerReload;

import play.i18n.Messages;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

import akka.actor.UntypedActor;

public class PlayerActor extends WebSocketActor {

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
		Iterator<WebSocketContext> iterator = getWebSockets();
		while(iterator.hasNext()){
			WebSocketContext ctx = iterator.next();
			ctx.getWebSocketOut().write(Json.stringify(Json.toJson(trigger)));
		}
		
	}
}
