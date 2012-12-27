package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Result index(){
		return ok(views.html.index.render());
	}
	
	public static Result navbar(){
		return ok(views.html.navbar.render());
	}
	
	public static Result player(){
		return ok(views.html.partials.player.render());
	}
	public static Result home(){
		return ok(views.html.partials.home.render());
	}
	public static Result tournametCreation(){
		return ok(views.html.partials.tournametCreation.render());
	}
}
