package controllers.message;

import play.i18n.Messages;

/**
 * The Class SimpleResponse contains a {@link #successMessage}, a
 * {@link #errorMessage} and a property {@link #successful}. <br/>
 * If {@link #successful} is <code>true</code>, than the {@link #successMessage}
 * is filled, otherwise the {@link #errorMessage} property gets filled.
 */
public class SimpleResponse extends DataContainer {

	private boolean successful = true;
	private String errorMessage;
	private String successMessage;

	@Override
	public DataContainer execute() {
		return this;
	}

	/**
	 * Create a success message
	 *
	 * @param successMessage the success message
	 * @return new {@link SimpleResponse} instance
	 */
	public static final SimpleResponse success(String successMessage) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(true);
		response.setSuccessMessage(successMessage);
		return response;
	}

	/**
	 * Create a Error Message
	 *
	 * @param errorMessage the error message
	 * @return new {@link SimpleResponse} instance
	 */
	public static final SimpleResponse error(String errorMessage) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(false);
		response.setErrorMessage(errorMessage);
		return response;
	}

	/**
	 * Create a technical error message
	 *
	 * @return new {@link SimpleResponse} instance
	 */
	public static final SimpleResponse technicalError() {
		return error(Messages.get("technical.error"));
	}

	/**
	 * Checks if {@code this} successful.
	 *
	 * @return true, if {@code this}  is successful
	 */
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets the success message.
	 *
	 * @return the success message
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

}
