package json.photo.appraisal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoAppraisalDTO {

	private int userId;
	private int photoId;

	private String appraisalBlockTitle;

	private boolean userCanAppraiseThePhoto;
	private String userCanNotAppraiseThePhotoText;

	private boolean userHasAlreadyAppraisedPhoto;

	private PhotoAppraisalForm photoAppraisalForm;

	private List<PhotoAppraisalResult> photoAppraisalResults;

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

	public boolean isUserCanAppraiseThePhoto() {
		return userCanAppraiseThePhoto;
	}

	public void setUserCanAppraiseThePhoto( final boolean userCanAppraiseThePhoto ) {
		this.userCanAppraiseThePhoto = userCanAppraiseThePhoto;
	}

	public String getUserCanNotAppraiseThePhotoText() {
		return userCanNotAppraiseThePhotoText;
	}

	public void setUserCanNotAppraiseThePhotoText( final String userCanNotAppraiseThePhotoText ) {
		this.userCanNotAppraiseThePhotoText = userCanNotAppraiseThePhotoText;
	}

	public PhotoAppraisalForm getPhotoAppraisalForm() {
		return photoAppraisalForm;
	}

	public void setPhotoAppraisalForm( final PhotoAppraisalForm photoAppraisalForm ) {
		this.photoAppraisalForm = photoAppraisalForm;
	}

	public void setAppraisalBlockTitle( final String appraisalBlockTitle ) {
		this.appraisalBlockTitle = appraisalBlockTitle;
	}

	public String getAppraisalBlockTitle() {
		return appraisalBlockTitle;
	}

	public List<PhotoAppraisalResult> getPhotoAppraisalResults() {
		return photoAppraisalResults;
	}

	public void setPhotoAppraisalResults( final List<PhotoAppraisalResult> photoAppraisalResults ) {
		this.photoAppraisalResults = photoAppraisalResults;
	}
}
