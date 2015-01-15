package rest.photo.list.bigPreview;

public class PhotoBigEntryDTO {

	private String photoName;
	private String photoImage;
	private String photoUploadDate;
	private String photoCategory;
	private String photoLink;
	private String photoCardLink;
	private String photoAuthorLink;
	private String photoDescription;

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
}
