package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalDTO {

	private final int photoId;
	private boolean userHasAlreadyAppraisedPhoto;

	private PhotoAppraisalForm photoAppraisalForm;

	public PhotoAppraisalDTO( final int photoId ) {
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
}
