package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.landing;

/**
 * Landing page controller
 * @author mpa
 */
public class Landing extends Controller{ 
	
	/**
	 * Renders the plain landing page
	 * @return landing page result
	 */
	public static Result land(){
		return ok(landing.render());
	}
}
