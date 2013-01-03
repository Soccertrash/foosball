package controllers.tournament.creation;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import controllers.WebSocketActor;
import controllers.message.DataContainer;
import controllers.message.SimpleResponse;
import controllers.message.TriggerReload;

public class TournamentCreationActor extends WebSocketActor {

	@Override
	public String handleMessage(String message) {
		JsonNode json = Json.parse(message);
		DataContainer dataContainer = Json.fromJson(json,
				DataContainer.class);
		DataContainer response = dataContainer.execute();
		if (response instanceof SimpleResponse) {
			if (((SimpleResponse) response).isSuccessful()) {
				getContext().self().tell(new TriggerReload());
			}
		}
		if(response != null){
			return Json.stringify(Json.toJson(response));
		}
		return null;
	}

	@Override
	public void handleTriggerReload(TriggerReload message) {
		// Don't reload the page
		
	}



}
