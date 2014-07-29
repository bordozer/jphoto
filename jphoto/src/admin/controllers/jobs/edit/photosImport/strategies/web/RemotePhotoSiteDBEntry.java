package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImportData;

public class RemotePhotoSiteDBEntry {

	private final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry;
	private final ImageToImportData imageToImportData;


	public RemotePhotoSiteDBEntry( final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry, final ImageToImportData imageToImportData ) {
		this.remotePhotoSitePhotoDiskEntry = remotePhotoSitePhotoDiskEntry;
		this.imageToImportData = imageToImportData;
	}

	public RemotePhotoSitePhotoDiskEntry getRemotePhotoSitePhotoDiskEntry() {
		return remotePhotoSitePhotoDiskEntry;
	}

	public ImageToImportData getImageToImportData() {
		return imageToImportData;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhotoDiskEntry );
	}
}
