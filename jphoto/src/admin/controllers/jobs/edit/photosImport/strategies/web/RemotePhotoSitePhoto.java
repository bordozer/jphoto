package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

import java.util.Date;
import java.util.List;

public class RemotePhotoSitePhoto {

	private final RemotePhotoSiteUser remotePhotoSiteUser;
	private final int photoId;
	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private String name;
	private Date uploadTime;
	private String imageUrl;

	private List<String> comments;

	private boolean isCached;

	public RemotePhotoSitePhoto( final RemotePhotoSiteUser remotePhotoSiteUser, final int photoId, final PhotosightCategory remotePhotoSiteCategory ) {
		this.remotePhotoSiteUser = remotePhotoSiteUser;
		this.photoId = photoId;
		this.remotePhotoSiteCategory = remotePhotoSiteCategory;
	}

	public RemotePhotoSiteUser getRemotePhotoSiteUser() {
		return remotePhotoSiteUser;
	}

	public int getPhotoId() {
		return photoId;
	}

	public RemotePhotoSiteCategory getRemotePhotoSiteCategory() {
		return remotePhotoSiteCategory;
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

		final RemotePhotoSitePhoto remotePhotoSitePhoto = ( RemotePhotoSitePhoto ) obj;
		return photoId == remotePhotoSitePhoto.getPhotoId();
	}
}
