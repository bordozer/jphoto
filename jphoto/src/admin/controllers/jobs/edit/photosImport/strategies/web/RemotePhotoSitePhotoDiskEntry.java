package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImport;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoData remotePhotoData;
	private final ImageToImport imageToImport;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoData remotePhotoData, final ImageToImport imageToImport ) {
		this.remotePhotoData = remotePhotoData;
		this.imageToImport = imageToImport;
	}

	public RemotePhotoData getRemotePhotoData() {
		return remotePhotoData;
	}

	public ImageToImport getImageToImport() {
		return imageToImport;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoData );
	}
}
