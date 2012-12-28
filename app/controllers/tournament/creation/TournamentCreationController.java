package controllers.tournament.creation;

import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import controllers.WebSocketActor;
import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.WebSocket;
import play.mvc.Http.Context;

public class TournamentCreationController extends Controller {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(TournamentCreationController.class);
	
	/** The player updater is used to send messages to the GUI */
	private static ActorRef SOCKET_HANDLER = Akka.system().actorOf(
			new Props(TournamentCreationActor.class));


	public static WebSocket<String> websocket() {
		final Context context = Http.Context.current();
		WebSocket<String> result = new WebSocket<String>() {

			@Override
			public void onReady(play.mvc.WebSocket.In<String> in,
					play.mvc.WebSocket.Out<String> out) {
				LOGGER.debug("Ready...");
				SOCKET_HANDLER.tell(new WebSocketActor.WebSocketContext(out, in, context));
				LOGGER.debug("... done");
			}
		};
		return result;
	}

}
