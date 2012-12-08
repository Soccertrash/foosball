package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Landing page controller
 * 
 * @author mpa
 */
public class Landing extends Controller {

	/**
	 * Renders the plain landing page
	 * 
	 * @return landing page result
	 */
	public static Result start() {
		return ok(views.html.landing.render());
	}

}
