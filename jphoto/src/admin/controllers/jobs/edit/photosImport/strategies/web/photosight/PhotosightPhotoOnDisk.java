package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;

public class PhotosightPhotoOnDisk {

	private final PhotosightPhoto photosightPhoto;
	private final ImageDiscEntry imageDiscEntry;

	public PhotosightPhotoOnDisk( final PhotosightPhoto photosightPhoto, final ImageDiscEntry imageDiscEntry ) {
		this.photosightPhoto = photosightPhoto;
		this.imageDiscEntry = imageDiscEntry;
	}

	public PhotosightPhoto getPhotosightPhoto() {
		return photosightPhoto;
	}

	public ImageDiscEntry getImageDiscEntry() {
		return imageDiscEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s", photosightPhoto );
	}
}
