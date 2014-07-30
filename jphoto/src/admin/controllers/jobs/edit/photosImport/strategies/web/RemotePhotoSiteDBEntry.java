package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImportData;

public class RemotePhotoSiteDBEntry {

	private final RemotePhoto remotePhoto;
	private final ImageToImportData imageToImportData;


	public RemotePhotoSiteDBEntry( final RemotePhoto remotePhoto, final ImageToImportData imageToImportData ) {
		this.remotePhoto = remotePhoto;
		this.imageToImportData = imageToImportData;
	}

	public RemotePhoto getRemotePhoto() {
		return remotePhoto;
	}

	public ImageToImportData getImageToImportData() {
		return imageToImportData;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhoto );
	}
}
