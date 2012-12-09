package controllers;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The Class JavascriptRouter contains the URLs for the Ajax requests (router).
 */
public class JavascriptRouter extends Controller {

	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter(
				"jsRoutes",
				// Routes
				controllers.player.routes.javascript.PlayerController.add(),
				controllers.player.routes.javascript.PlayerController
						.paginatorConfiguration(),
				controllers.player.routes.javascript.PlayerController
						.listenForUpdates(),
				controllers.player.routes.javascript.PlayerController.list()));
	}
}
