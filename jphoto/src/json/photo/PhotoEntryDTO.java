package json.photo;

public class PhotoEntryDTO {

	private final int photoId;

	private String groupOperationCheckbox;
	private String photoUploadDate;
	private String photoCategory;
	private String photoImage;
	private String photoContextMenu;
	private String photoMarks;
	private String photoName;
	private String photoAuthorLink;
	private String photoAuthorRank;

	public PhotoEntryDTO( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public String getGroupOperationCheckbox() {
		return groupOperationCheckbox;
	}

	public void setGroupOperationCheckbox( final String groupOperationCheckbox ) {
		this.groupOperationCheckbox = groupOperationCheckbox;
	}

	public String getPhotoUploadDate() {
		return photoUploadDate;
	}

	public void setPhotoUploadDate( final String photoUploadDate ) {
		this.photoUploadDate = photoUploadDate;
	}

	public String getPhotoCategory() {
		return photoCategory;
	}

	public void setPhotoCategory( final String photoCategory ) {
		this.photoCategory = photoCategory;
	}

	public String getPhotoImage() {
		return photoImage;
	}

	public void setPhotoImage( final String photoImage ) {
		this.photoImage = photoImage;
	}

	public String getPhotoContextMenu() {
		return photoContextMenu;
	}

	public void setPhotoContextMenu( final String photoContextMenu ) {
		this.photoContextMenu = photoContextMenu;
	}

	public String getPhotoMarks() {
		return photoMarks;
	}

	public void setPhotoMarks( final String photoMarks ) {
		this.photoMarks = photoMarks;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoAuthorLink() {
		return photoAuthorLink;
	}

	public void setPhotoAuthorLink( final String photoAuthorLink ) {
		this.photoAuthorLink = photoAuthorLink;
	}

	public String getPhotoAuthorRank() {
		return photoAuthorRank;
	}

	public void setPhotoAuthorRank( final String photoAuthorRank ) {
		this.photoAuthorRank = photoAuthorRank;
	}
}
