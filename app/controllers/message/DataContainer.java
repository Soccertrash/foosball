package controllers.message;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import controllers.player.message.AmountPlayers;
import controllers.player.message.CreatePlayer;
import controllers.player.message.DeletePlayer;
import controllers.player.message.PagedPlayer;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "method")
@JsonSubTypes({
		@Type(name = DataContainer.CREATE_PLAYER, value = CreatePlayer.class),
		@Type(name =  DataContainer.SIMPLE_RESPONSE, value = SimpleResponse.class),
		@Type(name =  DataContainer.AMOUNT_PLAYER, value = AmountPlayers.class) ,
		@Type(name =  DataContainer.PAGED_PLAYER, value = PagedPlayer.class) ,
		@Type(name =  DataContainer.ENTITY_LIST, value = EntityListContainer.class) ,
		@Type(name =  DataContainer.TRIGGER_RELOAD, value = TriggerReload.class) ,
		@Type(name =  DataContainer.DELETE_PLAYER, value = DeletePlayer.class) 
		})
public abstract class DataContainer {

	public final static String CREATE_PLAYER = "CREATE_PLAYER";
	public final static String SIMPLE_RESPONSE = "SIMPLE_RESPONSE";
	public final static String AMOUNT_PLAYER = "AMOUNT_PLAYER";
	public final static String PAGED_PLAYER = "PAGED_PLAYER";
	public final static String ENTITY_LIST = "ENTITY_LIST";
	public final static String DELETE_PLAYER = "DELETE_PLAYER";
	public final static String TRIGGER_RELOAD = "TRIGGER_RELOAD";

	public abstract DataContainer execute();
}
