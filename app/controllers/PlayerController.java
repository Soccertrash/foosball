package controllers;

import model.Player;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The Class PlayerController is used to CRUD the Player objects .
 */
public class PlayerController extends Controller {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PlayerController.class);

	public static Result index() {
		return ok(views.html.player.render());
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result add() {
		JsonNode jsonNode = request().body().asJson();
		LOGGER.trace("Incoming json {}", jsonNode);
		Form<Player> boundForm = form(Player.class).bind(jsonNode);
		LOGGER.trace("Form contains errors: {}", boundForm.hasErrors());
		if (boundForm.hasErrors()) {
			JsonNode errorsAsJson = boundForm.errorsAsJson();
			LOGGER.warn("Errors are (JSON) {}", errorsAsJson);

			// {"lastName":["%s muss befüllt sein"],"firstName":["%s muss befüllt sein"]}

			return badRequest(errorsAsJson);
		}

		return ok();
	}
}
