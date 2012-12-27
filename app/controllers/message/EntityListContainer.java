package controllers.message;

import java.util.List;

public class EntityListContainer<Type> extends DataContainer {
	
	private List<Type> entities; 

	@Override
	public DataContainer execute() {
		return this;
	}

	public List<Type> getEntities() {
		return entities;
	}

	public void setEntities(List<Type> entities) {
		this.entities = entities;
	}
	

}
