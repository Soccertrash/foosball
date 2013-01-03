package controllers.message;

import java.util.List;

/**
 * The Class EntityListContainer is a simple container for a list of Objects.
 *
 * @param <Type> the generic type
 */
public class EntityListContainer<Type> extends DataContainer {
	
	private List<Type> entities; 

	@Override
	public DataContainer execute() {
		return this;
	}

	/**
	 * Gets the entities (the list).
	 *
	 * @return the entities
	 */
	public List<Type> getEntities() {
		return entities;
	}

	public void setEntities(List<Type> entities) {
		this.entities = entities;
	}
	

}
