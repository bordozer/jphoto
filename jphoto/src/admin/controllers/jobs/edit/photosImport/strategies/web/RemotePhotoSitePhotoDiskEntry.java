package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImport;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final ImageToImport imageToImport;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final ImageToImport imageToImport ) {
		this.remotePhotoSitePhoto = remotePhotoSitePhoto;
		this.imageToImport = imageToImport;
	}

	public RemotePhotoSitePhoto getRemotePhotoSitePhoto() {
		return remotePhotoSitePhoto;
	}

	public ImageToImport getImageToImport() {
		return imageToImport;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhoto );
	}
}
