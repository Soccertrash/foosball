package controllers.player.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;
import model.Player;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;

public class AmountPlayers extends DataContainer {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AmountPlayers.class);

	private int size;

	@Override
	public DataContainer execute() {
		try {
			setSize(Player.finder.findRowCount());
		} catch (Exception e) {
			SimpleResponse sr = new SimpleResponse();
			sr.setSuccessful(false);
			sr.setErrorMessage(Messages.get("technical.error"));
			return sr;
		}
		return this;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
