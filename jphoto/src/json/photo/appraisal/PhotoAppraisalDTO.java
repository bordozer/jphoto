package json.photo.appraisal;

public class PhotoAppraisalDTO {

	private final int photoId;
	private boolean userHasAlreadyAppraisedPhoto;

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
}
