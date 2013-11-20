package controllers.photos.edit.description;

public class PhotoUploadDescription {

	private String uploadRuleDescription;
	private boolean passed = true;

	public String getUploadRuleDescription() {
		return uploadRuleDescription;
	}

	public void setUploadRuleDescription( final String uploadRuleDescription ) {
		this.uploadRuleDescription = uploadRuleDescription;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed( final boolean passed ) {
		this.passed = passed;
	}
}
