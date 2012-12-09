package controllers.player;

import java.util.HashMap;
import java.util.Map;

import model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Callback0;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import akka.actor.UntypedActor;
import controllers.player.PlayerDataUpdater.WebSocketContainer;

/**
 * The Class PlayerDataUpdater notifies all known webSockets about a
 * insertion/deletion/editing of a player
 */
public class PlayerDataUpdater extends UntypedActor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PlayerDataUpdater.class);

	private Map<WebSocket.In<String>, WebSocket.Out<String>> webSockets = new HashMap<>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WebSocketContainer) {
			final WebSocketContainer webSocketContainer = (WebSocketContainer) message;
			if (webSockets.containsKey(webSocketContainer.in)) {
				webSockets.remove(webSocketContainer.in).close();
			} else {
				webSocketContainer.in.onClose(new Callback0() {

					@Override
					public void invoke() throws Throwable {
						getContext().self().tell(webSocketContainer);

					}
				});
				webSockets.put(webSocketContainer.in, webSocketContainer.out);
			}
			LOGGER.debug("Amount of open WebSockets {}", webSockets.size());
		} else if (message instanceof Player) {
			for (WebSocket.Out<String> out : this.webSockets.values()) {
				out.write("player data changed");
			}
		}
	}

	/**
	 * The Class WebSocketContainer holds the In and Out of a WebSocket
	 */
	public static class WebSocketContainer {

		private final WebSocket.Out<String> out;
		private final WebSocket.In<String> in;

		public WebSocketContainer(Out<String> out, In<String> in) {
			super();
			this.out = out;
			this.in = in;
		}

	}
}
