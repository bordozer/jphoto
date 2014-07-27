package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final ImageDiscEntry imageDiscEntry;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final ImageDiscEntry imageDiscEntry ) {
		this.remotePhotoSitePhoto = remotePhotoSitePhoto;
		this.imageDiscEntry = imageDiscEntry;
	}

	public RemotePhotoSitePhoto getRemotePhotoSitePhoto() {
		return remotePhotoSitePhoto;
	}

	public ImageDiscEntry getImageDiscEntry() {
		return imageDiscEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhoto );
	}
}
