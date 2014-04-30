package json.photo.appraisal;

import core.general.photo.ValidationResult;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoAppraisalDTO {

	private int userId;
	private int photoId;

	private boolean userHasAlreadyAppraisedPhoto;

	private PhotoAppraisalForm photoAppraisalForm;
	private ValidationResult validationResult;
	private String appraisalBlockTitle;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public boolean isUserHasAlreadyAppraisedPhoto() {
		return userHasAlreadyAppraisedPhoto;
	}

	public void setUserHasAlreadyAppraisedPhoto( final boolean userHasAlreadyAppraisedPhoto ) {
		this.userHasAlreadyAppraisedPhoto = userHasAlreadyAppraisedPhoto;
	}

	public PhotoAppraisalForm getPhotoAppraisalForm() {
		return photoAppraisalForm;
	}

	public void setPhotoAppraisalForm( final PhotoAppraisalForm photoAppraisalForm ) {
		this.photoAppraisalForm = photoAppraisalForm;
	}

	public void setValidationResult( final ValidationResult validationResult ) {
		this.validationResult = validationResult;
	}

	public ValidationResult getValidationResult() {
		return validationResult;
	}

	public void setAppraisalBlockTitle( final String appraisalBlockTitle ) {
		this.appraisalBlockTitle = appraisalBlockTitle;
	}

	public String getAppraisalBlockTitle() {
		return appraisalBlockTitle;
	}
}
