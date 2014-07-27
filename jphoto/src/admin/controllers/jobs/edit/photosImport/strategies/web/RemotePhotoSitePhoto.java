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

	private boolean hasError;
	private String series;
	private int numberInSeries;

	public RemotePhotoSitePhoto( final RemotePhotoSiteUser remotePhotoSiteUser, final int photoId, final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
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

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError( final boolean hasError ) {
		this.hasError = hasError;
	}

	public void setSeries( final String series ) {
		this.series = series;
	}

	public String getSeries() {
		return series;
	}

	@Override
	public String toString() {
		return String.format( "Photosight photo #%d ( %s )", photoId, name );
	}

	@Override
	public int hashCode() {
		return photoId * 31 + imageUrl.hashCode();
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
		return photoId == remotePhotoSitePhoto.getPhotoId() && imageUrl.equals( remotePhotoSitePhoto.getImageUrl() );
	}

	public int getNumberInSeries() {
		return numberInSeries;
	}

	public void setNumberInSeries( final int numberInSeries ) {
		this.numberInSeries = numberInSeries;
	}
}
