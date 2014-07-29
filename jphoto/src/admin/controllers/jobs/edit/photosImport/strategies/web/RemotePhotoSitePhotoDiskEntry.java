package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.RemoteImageDiscEntry;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final RemoteImageDiscEntry remoteImageDiscEntry;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final RemoteImageDiscEntry remoteImageDiscEntry ) {
		this.remotePhotoSitePhoto = remotePhotoSitePhoto;
		this.remoteImageDiscEntry = remoteImageDiscEntry;
	}

	public RemotePhotoSitePhoto getRemotePhotoSitePhoto() {
		return remotePhotoSitePhoto;
	}

	public RemoteImageDiscEntry getRemoteImageDiscEntry() {
		return remoteImageDiscEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhoto );
	}
}
