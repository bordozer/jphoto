package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalDTO {

	private final int photoId;
	private boolean userHasAlreadyAppraisedPhoto;

	private List<PhotoAppraisalCategory> photoVotingCategories;

	private int minAccessibleMarkForGenre;
	private int maxAccessibleMarkForGenre;
	private List<Integer> appraisalCategories;

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

	public void setPhotoVotingCategories( final List<PhotoAppraisalCategory> photoVotingCategories ) {
		this.photoVotingCategories = photoVotingCategories;
	}

	public List<PhotoAppraisalCategory> getPhotoVotingCategories() {
		return photoVotingCategories;
	}

	public void setMinAccessibleMarkForGenre( final int minAccessibleMarkForGenre ) {
		this.minAccessibleMarkForGenre = minAccessibleMarkForGenre;
	}

	public int getMinAccessibleMarkForGenre() {
		return minAccessibleMarkForGenre;
	}

	public void setMaxAccessibleMarkForGenre( final int maxAccessibleMarkForGenre ) {
		this.maxAccessibleMarkForGenre = maxAccessibleMarkForGenre;
	}

	public int getMaxAccessibleMarkForGenre() {
		return maxAccessibleMarkForGenre;
	}

	public void setAppraisalCategories( final List<Integer> appraisalCategories ) {
		this.appraisalCategories = appraisalCategories;
	}

	public List<Integer> getAppraisalCategories() {
		return appraisalCategories;
	}
}
