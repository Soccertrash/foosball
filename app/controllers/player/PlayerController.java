package controllers.player;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Player;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.db.ebean.Transactional;
import play.i18n.Messages;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.actors.threadpool.Arrays;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.avaje.ebean.Query;

import controllers.pagination.PaginationConfiguration;
import controllers.player.PlayerDataUpdater.WebSocketContainer;

/**
 * The Class PlayerController is used to CRUD the Player objects .
 */
public class PlayerController extends Controller {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PlayerController.class);

	/** The player updater is used to send messages to the GUI */
	static ActorRef playerUpdater = Akka.system().actorOf(
			new Props(PlayerDataUpdater.class));

	private final static int PAGE_SIZE = 10;

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
		Player player = boundForm.get();
		player.save();
		ObjectNode result = play.libs.Json.newObject();
		result.put("result", Messages.get("player.successful"));
		playerUpdater.tell(player);
		return ok(result);
	}

	@Transactional
	public static Result list(Integer page) {
		Map<String, String[]> queryString = request().queryString();
		String orderByCol = null;
		Boolean isAsc = true;
		for (Entry<String, String[]> entry : queryString.entrySet()) {
			String key = entry.getKey();
			LOGGER.trace("Key {} Value {}", key,
					Arrays.toString(entry.getValue()));
			switch (key) {
			case "lastName":
			case "firstName":
			case "nickName":
				orderByCol = key;
				break;
			}
			if (orderByCol != null) {
				if (entry.getValue() != null && entry.getValue().length > 0) {
					isAsc = Boolean.valueOf(entry.getValue()[0]);
					break;
				}
			}

		}
		LOGGER.trace("start list with page {}", page);
		int first = (page - 1) * PAGE_SIZE;
		int max = first + PAGE_SIZE;
		Query<Player> query = Player.finder.setFirstRow(first).setMaxRows(max);
		if (orderByCol != null) {
			LOGGER.trace("Order by {} {}", orderByCol, isAsc);
			if (isAsc) {
				query.orderBy().asc(orderByCol);
			} else {
				query.orderBy().desc(orderByCol);
			}
		}

		List<Player> findList = query.findList();
		LOGGER.trace("Found {}", findList);
		return ok(Json.toJson(findList));
	}

	public static Result paginatorConfiguration() {
		PaginationConfiguration configuration = new PaginationConfiguration.Builder()
				.pageSize(PAGE_SIZE)
				.addColumn("lastName", Messages.get("lastName"), true)
				.addColumn("firstName", Messages.get("firstName"), true)
				.addColumn("nickname", Messages.get("nickname"), true)
				.rowAmount(Player.finder.findRowCount()).build();
		return ok(Json.toJson(configuration));
	}

	public static WebSocket<String> listenForUpdates() {
		LOGGER.trace("Entered listenForUpdates");
		WebSocket<String> result = new WebSocket<String>() {

			@Override
			public void onReady(play.mvc.WebSocket.In<String> in,
					play.mvc.WebSocket.Out<String> out) {
				playerUpdater.tell(new WebSocketContainer(out, in));
			}
		};
		return result;
	}
}