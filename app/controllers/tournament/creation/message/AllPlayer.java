package controllers.tournament.creation.message;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Player;

import controllers.message.DataContainer;
import controllers.message.SimpleResponse;

public class AllPlayer extends DataContainer {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(AllPlayer.class);

	private List<Player> players;

	@Override
	public DataContainer execute() {
		try {
			players = Player.finder.all();
			return this;
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			return SimpleResponse.technicalError();
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

}
