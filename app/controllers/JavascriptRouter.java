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
				controllers.routes.javascript.PlayerController.add(),
				controllers.routes.javascript.PlayerController
						.paginatorConfiguration(),
				controllers.routes.javascript.PlayerController.list()));
	}
}
