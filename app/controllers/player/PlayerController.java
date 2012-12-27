package controllers.player;

import model.Player;

import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;

import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.WebSocket;
import controllers.player.PlayerActor.WebSocketContext;

public class PlayerController extends Controller {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PlayerController.class);
	
	/** The player updater is used to send messages to the GUI */
	private static ActorRef SOCKET_HANDLER = Akka.system().actorOf(
			new Props(PlayerActor.class));


	public static WebSocket<String> websocket() {
		final Context context = Http.Context.current();
		WebSocket<String> result = new WebSocket<String>() {

			@Override
			public void onReady(play.mvc.WebSocket.In<String> in,
					play.mvc.WebSocket.Out<String> out) {
				LOGGER.debug("Ready...");
				SOCKET_HANDLER.tell(new WebSocketContext(out, in, context));
				LOGGER.debug("... done");
			}
		};
		return result;
	}
	
}
