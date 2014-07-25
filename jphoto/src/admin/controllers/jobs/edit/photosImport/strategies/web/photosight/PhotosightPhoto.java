package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import java.util.Date;
import java.util.List;

public class PhotosightPhoto {

	private final PhotosightUser photosightUser;
	private final int photoId;
	private final PhotosightCategory photosightCategory;
	private String name;
	private Date uploadTime;
	private String imageUrl;

	private List<String> comments;

	private boolean isCached;

	public PhotosightPhoto( final PhotosightUser photosightUser, final int photoId, final PhotosightCategory photosightCategory ) {
		this.photosightUser = photosightUser;
		this.photoId = photoId;
		this.photosightCategory = photosightCategory;
	}

	public PhotosightUser getPhotosightUser() {
		return photosightUser;
	}

	public int getPhotoId() {
		return photoId;
	}

	public PhotosightCategory getPhotosightCategory() {
		return photosightCategory;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime( final Date uploadTime ) {
		this.uploadTime = uploadTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl( final String imageUrl ) {
		this.imageUrl = imageUrl;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments( final List<String> comments ) {
		this.comments = comments;
	}

	public boolean isCached() {
		return isCached;
	}

	public void setCached( final boolean cached ) {
		isCached = cached;
	}

	@Override
	public String toString() {
		return String.format( "Photosight photo #%d", photoId );
	}

	@Override
	public int hashCode() {
		return photoId * 31;
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

		final PhotosightPhoto photosightPhoto = ( PhotosightPhoto ) obj;
		return photoId == photosightPhoto.getPhotoId();
	}
}
