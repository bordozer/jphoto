package core.general.photo;

public class ValidationResult {

	private boolean validationPassed = true;
	private String validationMessage;

	public boolean isValidationPassed() {
		return validationPassed;
	}

	public boolean isValidationFailed() {
		return ! validationPassed;
	}

	public void setValidationPassed( final boolean validationPassed ) {
		this.validationPassed = validationPassed;
	}

	public void failValidation( final String validationMessage ) {
		validationPassed = false;
		this.validationMessage = validationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage( final String validationMessage ) {
		this.validationMessage = validationMessage;
	}
}
