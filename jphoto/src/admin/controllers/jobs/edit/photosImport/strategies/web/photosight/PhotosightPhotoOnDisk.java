package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhoto;

public class PhotosightPhotoOnDisk {

	private final RemotePhotoSitePhoto remotePhotoSitePhoto;
	private final ImageDiscEntry imageDiscEntry;

	public PhotosightPhotoOnDisk( final RemotePhotoSitePhoto remotePhotoSitePhoto, final ImageDiscEntry imageDiscEntry ) {
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
