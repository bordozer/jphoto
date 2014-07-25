package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImport;

public class RemotePhotoSiteDBEntry {

	private final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry;
	private final ImageToImport imageToImport;


	public RemotePhotoSiteDBEntry( final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry, final ImageToImport imageToImport ) {
		this.remotePhotoSitePhotoDiskEntry = remotePhotoSitePhotoDiskEntry;
		this.imageToImport = imageToImport;
	}

	public RemotePhotoSitePhotoDiskEntry getRemotePhotoSitePhotoDiskEntry() {
		return remotePhotoSitePhotoDiskEntry;
	}

	public ImageToImport getImageToImport() {
		return imageToImport;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhotoDiskEntry );
	}
}
