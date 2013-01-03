package controllers.player.message;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;
import model.Player;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;

/**
 * The Class CreatePlayer is used for creating a new player
 */
public class CreatePlayer extends DataContainer {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CreatePlayer.class);

	private Player player;
	
	@JsonIgnore
	private SimpleResponse response = new SimpleResponse();

	@Override
	public DataContainer execute() {
		
		if (getPlayer() == null) {
			error();
			return response;
		}
		try {
			getPlayer().save();
			success();
		} catch (Exception e) {
			LOGGER.error("Player could not be saved: {}",e.getMessage());
			error();
		}
		return response;
	}
	
	private void error(){
		response.setErrorMessage(Messages.get("technical.error"));
		response.setSuccessful(false);
	}
	
	private void success(){
		response.setSuccessMessage(Messages.get("player.save"));
		response.setSuccessful(true);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
