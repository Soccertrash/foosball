package controllers.player.message;

import java.util.ArrayList;
import java.util.List;

import model.Player;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;

import controllers.message.DataContainer;
import controllers.message.EntityListContainer;
import controllers.message.SimpleResponse;

public class PagedPlayer extends DataContainer {
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(PagedPlayer.class);

	private int from;
	private int maxRows;
	
	@JsonIgnore
	private DataContainer response;
	
	@Override
	public DataContainer execute() {
		if(!check()){
			return response;
		}
		EntityListContainer<Player> listContainer = new EntityListContainer<>();
		try{
			listContainer.setEntities(Player.finder.setMaxRows(getMaxRows()).setFirstRow(getFrom()).findList());
			response = listContainer;
		}catch(Exception e){
			LOGGER.error("Exception: {}",e.getMessage());
			error();
		}
		
		return response;
	}
	
	
	private boolean check(){
		if(getFrom() < 0 || getMaxRows() <0){
			error();
			return false;
		}
		return true;
	}
	
	private void error(){
		SimpleResponse response =  new SimpleResponse();
		response.setSuccessful(false);
		response.setErrorMessage(Messages.get("technical.error"));
		this.response = response;
	}


	public int getFrom() {
		return from;
	}


	public void setFrom(int from) {
		this.from = from;
	}


	public int getMaxRows() {
		return maxRows;
	}


	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	
}
