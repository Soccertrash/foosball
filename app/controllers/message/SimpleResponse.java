package controllers.message;

import play.i18n.Messages;

public class SimpleResponse extends DataContainer {

	private boolean successful = true;
	private String errorMessage;
	private String successMessage;

	@Override
	public DataContainer execute() {
		return this;
	}

	public static final SimpleResponse success(String successMessage) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(true);
		response.setSuccessMessage(successMessage);
		return response;
	}

	public static final SimpleResponse error(String errorMessage) {
		SimpleResponse response = new SimpleResponse();
		response.setSuccessful(false);
		response.setErrorMessage(errorMessage);
		return response;
	}
	public static final SimpleResponse technicalError() {
		return error(Messages.get("technical.error"));
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

}
