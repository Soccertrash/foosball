package controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Http.Context;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import akka.actor.UntypedActor;
import controllers.message.TriggerReload;

/**
 * The Class WebSocketActor manages the WebSockets for a certain domain. 
 */
public abstract class WebSocketActor extends UntypedActor {
	private final Map<In<String>, WebSocketContext> webSockets = new HashMap<>();
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WebSocketContext) {
			WebSocketContext context = (WebSocketContext) message;

			if (this.webSockets.containsKey(context.webSocketIn)) {
				context.webSocketOut.close();
				this.webSockets.remove(context.webSocketIn);
				this.LOGGER.debug("Removed WS... ");
			} else {
				final In<String> webSocketIn = context.webSocketIn;
				webSocketIn.onClose(new Callback0() {

					@Override
					public void invoke() throws Throwable {
						WebSocketContext webSocketContext = WebSocketActor.this.webSockets
								.get(webSocketIn);
						WebSocketActor.this.LOGGER.debug("Closing... {}",
								webSocketContext);
						WebSocketActor.this.getContext().self()
								.tell(webSocketContext);

					}
				});
				context.webSocketIn.onMessage(new WebSocketInCallback(context));
				this.webSockets.put(context.webSocketIn, context);
			}
		} else if (message instanceof TriggerReload) {
			this.handleTriggerReload((TriggerReload)message);
		}
		this.LOGGER.debug("Amount of WebSockets {}", this.webSockets.size());

	}

	private class WebSocketInCallback implements Callback<String> {

		private WebSocketContext context;

		private WebSocketInCallback(WebSocketContext context) {
			super();
			this.context = context;
		}

		@Override
		public void invoke(String message) throws Throwable {
			Context.current.set(this.context.context);
			try {
				WebSocketActor.this.LOGGER.debug("Incoming {}", message);
				String response = WebSocketActor.this.handleMessage(message);
				if (response != null) {
					this.context.webSocketOut.write(response);
				}

			} catch (Exception e) {
				WebSocketActor.this.LOGGER
						.error("Exception has been thrown", e);
			} finally {
				Context.current.remove();
			}
		}

	}

	/**
	 * Handle the incoming message
	 * 
	 * @param message
	 *            the message that is received via web sockets
	 * @return the string that is send back to the client or <code>null</code>
	 */
	public abstract String handleMessage(String message);

	/**
	 * Handle trigger reload message
	 */
	public abstract void handleTriggerReload(TriggerReload message);

	/**
	 * Gets the web sockets (iterator)
	 * 
	 * @return a {@link Iterator} of {@link WebSocketContext}s
	 */
	protected Iterator<WebSocketContext> getWebSockets() {
		return this.webSockets.values().iterator();
	}

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

		public Out<String> getWebSocketOut() {
			return webSocketOut;
		}
		

	}
}
