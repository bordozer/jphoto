package ui.dtos;

public class AjaxResultDTO {

	private boolean isSuccessful;
	private String message;

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful( final boolean successful ) {
		isSuccessful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( final String message ) {
		this.message = message;
	}

	public static AjaxResultDTO successResult() {
		final AjaxResultDTO resultDTO = new AjaxResultDTO();
		resultDTO.setSuccessful( true );

		return resultDTO;
	}

	public static AjaxResultDTO failResult( final String message ) {
		final AjaxResultDTO resultDTO = new AjaxResultDTO();

		resultDTO.setSuccessful( false );
		resultDTO.setMessage( message );

		return resultDTO;
	}
}
