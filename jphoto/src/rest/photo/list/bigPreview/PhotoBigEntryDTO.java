package rest.photo.list.bigPreview;

public class PhotoBigEntryDTO {

	private String photoName;
	private String photoImageUrl;
	private String photoUploadDate;
	private String photoCategory;
	private String photoLink;
	private String photoCardLink;
	private String photoAuthorLink;
	private String photoDescription;

	private int photoImageWidth;
	private int photoImageHeight;
	private int minContainerWidth;

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

	public String getPhotoImageUrl() {
		return photoImageUrl;
	}

	public void setPhotoImageUrl( final String photoImageUrl ) {
		this.photoImageUrl = photoImageUrl;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoLink() {
		return photoLink;
	}

	public void setPhotoLink( final String photoLink ) {
		this.photoLink = photoLink;
	}

	public String getPhotoCardLink() {
		return photoCardLink;
	}

	public void setPhotoCardLink( final String photoCardLink ) {
		this.photoCardLink = photoCardLink;
	}

	public String getPhotoAuthorLink() {
		return photoAuthorLink;
	}

	public void setPhotoAuthorLink( final String photoAuthorLink ) {
		this.photoAuthorLink = photoAuthorLink;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription( final String photoDescription ) {
		this.photoDescription = photoDescription;
	}

	public int getPhotoImageWidth() {
		return photoImageWidth;
	}

	public void setPhotoImageWidth( final int photoImageWidth ) {
		this.photoImageWidth = photoImageWidth;
	}

	public int getPhotoImageHeight() {
		return photoImageHeight;
	}

	public void setPhotoImageHeight( final int photoImageHeight ) {
		this.photoImageHeight = photoImageHeight;
	}

	public void setMinContainerWidth( final int minContainerWidth ) {
		this.minContainerWidth = minContainerWidth;
	}

	public int getMinContainerWidth() {
		return minContainerWidth;
	}
}
