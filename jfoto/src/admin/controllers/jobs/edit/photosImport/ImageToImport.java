package admin.controllers.jobs.edit.photosImport;

import core.general.photo.Photo;
import core.general.user.User;

import java.util.Date;

public class ImageToImport {

	private final ImageDiscEntry imageDiscEntry;

	private String name;

	private User user;
	private Date uploadTime;
	private String photoDescription;
	private String photoKeywords;
	private Photo photo;

	private int importId;

	public ImageToImport( final ImageDiscEntry imageDiscEntry ) {
		this.imageDiscEntry = imageDiscEntry;
	}

	public ImageDiscEntry getImageDiscEntry() {
		return imageDiscEntry;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime( final Date uploadTime ) {
		this.uploadTime = uploadTime;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription( final String photoDescription ) {
		this.photoDescription = photoDescription;
	}

	public String getPhotoKeywords() {
		return photoKeywords;
	}

	public void setPhotoKeywords( final String photoKeywords ) {
		this.photoKeywords = photoKeywords;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public int getImportId() {
		return importId;
	}

	public void setImportId( final int importId ) {
		this.importId = importId;
	}

	@Override
	public String toString() {
		return String.format( "Photo to import: %s", imageDiscEntry );
	}

	@Override
	public int hashCode() {
		return 31 * imageDiscEntry.hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( this == obj ) {
			return true;
		}

		if ( obj == null ) {
			return false;
		}

		if ( ! this.getClass().equals( obj.getClass() ) ) {
			return false;
		}

		final ImageToImport imageToImport = ( ImageToImport ) obj;
		return imageDiscEntry == imageToImport.getImageDiscEntry();
	}
}

