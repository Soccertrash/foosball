package controllers.tournament.creation.message;

import model.Player;
import play.i18n.Messages;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;

public class CreateTournament extends DataContainer {

	private String tournamentName;
	private Player[] selectedPlayers;

	@Override
	public DataContainer execute() {
		return SimpleResponse.success(Messages.get("tournamentcreation.tournament.creation.successful"));
	}

	public String getTournamentName() {
		return tournamentName;
	}

	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}

	public Player[] getSelectedPlayers() {
		return selectedPlayers;
	}

	public void setSelectedPlayers(Player[] selectedPlayers) {
		this.selectedPlayers = selectedPlayers;
	}

}
