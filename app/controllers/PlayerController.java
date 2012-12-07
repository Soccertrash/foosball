package controllers;

import java.util.List;

import model.Player;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.db.ebean.Transactional;
import play.i18n.Messages;
import play.libs.Json;
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
	@Transactional
	public static Result add() {
		JsonNode jsonNode = request().body().asJson();
		LOGGER.trace("Incoming json {}", jsonNode);
		Form<Player> boundForm = form(Player.class).bind(jsonNode);
		LOGGER.trace("Form contains errors: {}", boundForm.hasErrors());
		if (boundForm.hasErrors()) {
			JsonNode errorsAsJson = boundForm.errorsAsJson();
			LOGGER.warn("Errors are (JSON) {}", errorsAsJson);
			return badRequest(errorsAsJson);
		}
		boundForm.get().save();
		ObjectNode result = play.libs.Json.newObject();
		result.put("result", Messages.get("player.successful"));
		return ok(result);
	}

	@Transactional
	public static Result list(Integer first, Integer max) {
		LOGGER.trace("start list with first={} and max={}");
		List<Player> findList = Player.finder.setFirstRow(first)
				.setMaxRows(max).findList();
		LOGGER.trace("Found {}", findList);
		return ok(Json.toJson(findList));
	}

	@Transactional
	public static Result count() {
		return ok(String.valueOf(Player.finder.findRowCount()));
	}
}
