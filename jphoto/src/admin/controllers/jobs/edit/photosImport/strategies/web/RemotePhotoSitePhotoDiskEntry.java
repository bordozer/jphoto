package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.RemoteImageLocalEntry;

public class RemotePhotoSitePhotoDiskEntry {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final RemoteImageLocalEntry remoteImageLocalEntry;

	public RemotePhotoSitePhotoDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final RemoteImageLocalEntry remoteImageLocalEntry ) {
		this.remotePhotoSitePhoto = remotePhotoSitePhoto;
		this.remoteImageLocalEntry = remoteImageLocalEntry;
	}

	public RemotePhotoSitePhoto getRemotePhotoSitePhoto() {
		return remotePhotoSitePhoto;
	}

	public RemoteImageLocalEntry getRemoteImageLocalEntry() {
		return remoteImageLocalEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s", remotePhotoSitePhoto );
	}
}
