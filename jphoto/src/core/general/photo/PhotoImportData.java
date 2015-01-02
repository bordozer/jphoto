package core.general.photo;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;

public class PhotoImportData {

	private final PhotosImportSource photosImportSource;
	private final String remoteUserId;
	private final String remoteUserName;
	private final int remotePhotoId;

	public PhotoImportData( final PhotosImportSource photosImportSource, final String remoteUserId, final String remoteUserName, final int remotePhotoId ) {
		this.photosImportSource = photosImportSource;
		this.remoteUserId = remoteUserId;
		this.remoteUserName = remoteUserName;
		this.remotePhotoId = remotePhotoId;
	}

	public PhotosImportSource getPhotosImportSource() {
		return photosImportSource;
	}

	public String getRemoteUserId() {
		return remoteUserId;
	}

	public String getRemoteUserName() {
		return remoteUserName;
	}

	public int getRemotePhotoId() {
		return remotePhotoId;
	}
}
