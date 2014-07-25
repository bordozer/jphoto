package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.ImageToImport;

public class PhotosightDBEntry {

	private final PhotosightPhotoOnDisk photosightPhotoOnDisk;
	private final ImageToImport imageToImport;


	public PhotosightDBEntry( final PhotosightPhotoOnDisk photosightPhotoOnDisk, final ImageToImport imageToImport ) {
		this.photosightPhotoOnDisk = photosightPhotoOnDisk;
		this.imageToImport = imageToImport;
	}

	public PhotosightPhotoOnDisk getPhotosightPhotoOnDisk() {
		return photosightPhotoOnDisk;
	}

	public ImageToImport getImageToImport() {
		return imageToImport;
	}

	@Override
	public String toString() {
		return String.format( "%s", photosightPhotoOnDisk );
	}
}
