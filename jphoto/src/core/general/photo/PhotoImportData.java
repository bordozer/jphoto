package core.general.photo;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;

public class PhotoImportData {

	private final PhotosImportSource photosImportSource;
	private final String remoteUserId;
	private final int remotePhotoId;

	public PhotoImportData( final PhotosImportSource photosImportSource, final String remoteUserId, final int remotePhotoId ) {
		this.photosImportSource = photosImportSource;
		this.remoteUserId = remoteUserId;
		this.remotePhotoId = remotePhotoId;
	}

	public PhotosImportSource getPhotosImportSource() {
		return photosImportSource;
	}

	public String getRemoteUserId() {
		return remoteUserId;
	}

	public int getRemotePhotoId() {
		return remotePhotoId;
	}
}
