package admin.controllers.jobs.edit.photosImport;

import core.general.photo.Photo;
import core.general.user.User;

import java.util.Date;

public class ImageToImport {

	private final RemoteImageDiscEntry remoteImageDiscEntry;

	private String name;

	private User user;
	private Date uploadTime;
	private String photoDescription;
	private String photoKeywords;
	private Photo photo;

	private int importId;
	private RemotePhotoSiteSeries remotePhotoSiteSeries;

	public ImageToImport( final RemoteImageDiscEntry remoteImageDiscEntry ) {
		this.remoteImageDiscEntry = remoteImageDiscEntry;
	}

	public RemoteImageDiscEntry getRemoteImageDiscEntry() {
		return remoteImageDiscEntry;
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

	public void setRemotePhotoSiteSeries( final RemotePhotoSiteSeries remotePhotoSiteSeries ) {
		this.remotePhotoSiteSeries = remotePhotoSiteSeries;
	}

	public RemotePhotoSiteSeries getRemotePhotoSiteSeries() {
		return remotePhotoSiteSeries;
	}

	@Override
	public String toString() {
		return String.format( "Photo to import: %s", remoteImageDiscEntry );
	}

	@Override
	public int hashCode() {
		return 31 * remoteImageDiscEntry.hashCode();
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
		return remoteImageDiscEntry == imageToImport.getRemoteImageDiscEntry();
	}
}

