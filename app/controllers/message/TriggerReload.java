package controllers.message;

import java.io.Serializable;

/**
 * The Class TriggerReload is used for getting the current view reloaded.
 */
public class TriggerReload extends DataContainer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public DataContainer execute() {
		return this;
	}

}
