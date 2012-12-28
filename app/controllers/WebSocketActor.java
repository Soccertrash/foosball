package controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import akka.actor.UntypedActor;
import controllers.WebSocketActor.WebSocketContext;
import controllers.message.TriggerReload;

public abstract class WebSocketActor extends UntypedActor {
	private final Map<In<String>, WebSocketContext> webSockets = new HashMap<>();
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WebSocketContext) {
			WebSocketContext context = (WebSocketContext) message;

			if (webSockets.containsKey(context.webSocketIn)) {
				context.webSocketOut.close();
				webSockets.remove(context.webSocketIn);
				LOGGER.debug("Removed WS... ");
			} else {
				final In<String> webSocketIn = context.webSocketIn;
				webSocketIn.onClose(new Callback0() {

					@Override
					public void invoke() throws Throwable {
						WebSocketContext webSocketContext = webSockets
								.get(webSocketIn);
						LOGGER.debug("Closing... {}", webSocketContext);
						getContext().self().tell(webSocketContext);

					}
				});
				context.webSocketIn.onMessage(new WebSocketInCallback(context));
				webSockets.put(context.webSocketIn, context);
			}
		} else if (message instanceof TriggerReload) {
			for (WebSocketContext ctx : this.webSockets.values()) {
				ctx.webSocketOut.write(Json.stringify(Json.toJson(message)));
			}
		}
		LOGGER.debug("Amount of WebSockets {}", webSockets.size());

	}

	private class WebSocketInCallback implements Callback<String> {

		private WebSocketContext context;

		private WebSocketInCallback(WebSocketContext context) {
			super();
			this.context = context;
		}

		@Override
		public void invoke(String message) throws Throwable {
			Context.current.set(context.context);
			try {
				LOGGER.debug("Incoming {}", message);
				String response = handleMessage(message);
				if (response != null) {
					context.webSocketOut.write(response);
				}

			} catch (Exception e) {
				LOGGER.error("Exception has been thrown", e);
			} finally {
				Context.current.remove();
			}
		}

	}

	public abstract String handleMessage(String message);

	public static class WebSocketContext {
		private final Out<String> webSocketOut;
		private final In<String> webSocketIn;
		private Context context;

		public WebSocketContext(Out<String> webSocketOut,
				In<String> webSocketIn, Context context) {
			super();
			this.webSocketOut = webSocketOut;
			this.webSocketIn = webSocketIn;
			this.context = context;
		}

	}
}
