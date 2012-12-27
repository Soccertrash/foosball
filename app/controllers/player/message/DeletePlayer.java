package controllers.player.message;

import model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;

import controllers.message.DataContainer;
import controllers.message.SimpleResponse;

public class DeletePlayer extends DataContainer {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(DeletePlayer.class);
	private long playerId;

	@Override
	public DataContainer execute() {
		Player player = Player.finder.byId(playerId);
		if (player == null) {
			return SimpleResponse.error(Messages.get("player.exists.not"));
		}
		try {
			player.delete();
		} catch (Exception e) {
			LOGGER.error("Error: {}", e.getMessage());
			return SimpleResponse.technicalError();
		}
		return SimpleResponse.success(Messages.get("player.delete",
				player.getLastName() + ", " + player.getFirstName()));
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
