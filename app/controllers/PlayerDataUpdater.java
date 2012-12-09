package controllers;

import java.util.HashMap;
import java.util.Map;

import model.Player;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import akka.actor.UntypedActor;
import controllers.PlayerDataUpdater.WebSocketContainer;

public class PlayerDataUpdater extends UntypedActor {

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
		} else if (message instanceof Player) {
			for (WebSocket.Out<String> out : this.webSockets.values()) {
				out.write("player data changed");
			}
		}
	}

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
