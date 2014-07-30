package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.RemotePhotoSiteSeries;

import java.util.Date;
import java.util.List;

public class RemotePhotoSitePhoto {

	private final RemoteUser remoteUser;
	private final int photoId;
	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private String name;
	private Date uploadTime;
	private RemotePhotoSiteImage remotePhotoSiteImage;

	private List<String> comments;

	private boolean isCached;

	private boolean hasError;
	private RemotePhotoSiteSeries remotePhotoSiteSeries;
	private int numberInSeries;

	public RemotePhotoSitePhoto( final RemoteUser remoteUser, final int photoId, final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		this.remoteUser = remoteUser;
		this.photoId = photoId;
		this.remotePhotoSiteCategory = remotePhotoSiteCategory;
	}

	public RemoteUser getRemoteUser() {
		return remoteUser;
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

	public RemotePhotoSiteImage getRemotePhotoSiteImage() {
		return remotePhotoSiteImage;
	}

	public void setRemotePhotoSiteImage( final RemotePhotoSiteImage remotePhotoSiteImage ) {
		this.remotePhotoSiteImage = remotePhotoSiteImage;
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

	public void setRemotePhotoSiteSeries( final RemotePhotoSiteSeries remotePhotoSiteSeries ) {
		this.remotePhotoSiteSeries = remotePhotoSiteSeries;
	}

	public RemotePhotoSiteSeries getRemotePhotoSiteSeries() {
		return remotePhotoSiteSeries;
	}

	@Override
	public String toString() {
		return String.format( "Photosight photo #%d ( %s )", photoId, name );
	}

	@Override
	public int hashCode() {
		return photoId * 31 + remotePhotoSiteImage.hashCode();
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
		return photoId == remotePhotoSitePhoto.getPhotoId() && remotePhotoSiteImage.equals( remotePhotoSitePhoto.getRemotePhotoSiteImage() );
	}

	public int getNumberInSeries() {
		return numberInSeries;
	}

	public void setNumberInSeries( final int numberInSeries ) {
		this.numberInSeries = numberInSeries;
	}
}
