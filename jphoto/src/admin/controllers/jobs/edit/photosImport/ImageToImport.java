package admin.controllers.jobs.edit.photosImport;

import core.general.photo.PhotoImageLocationType;

import java.io.File;

public class ImageToImport {

	private final PhotosImportSource photosImportSource;
	private final String genreName;
	private final File imageFile;
	private final PhotoImageLocationType photoImageLocationType;

	private String photoImageUrl;

	public ImageToImport( final PhotosImportSource photosImportSource, final PhotoImageLocationType photoImageLocationType, final String genreName, final File imageFile ) {
		this.photosImportSource = photosImportSource;
		this.genreName = genreName;
		this.imageFile = imageFile;

		this.photoImageLocationType = photoImageLocationType;
	}

	public ImageToImport( final PhotosImportSource photosImportSource, final PhotoImageLocationType photoImageLocationType, final String genreName, final File remotePhotoCacheFile, final String photoImageUrl ) {
		this( photosImportSource, photoImageLocationType, genreName, remotePhotoCacheFile );
		this.photoImageUrl = photoImageUrl;
	}

	public PhotosImportSource getPhotosImportSource() {
		return photosImportSource;
	}

	public String getGenreName() {
		return genreName;
	}

	public File getImageFile() {
		return imageFile;
	}

	public String getPhotoImageUrl() {
		return photoImageUrl;
	}

	public void setPhotoImageUrl( final String photoImageUrl ) {
		this.photoImageUrl = photoImageUrl;
	}

	public PhotoImageLocationType getPhotoImageLocationType() {
		return photoImageLocationType;
	}

	@Override
	public String toString() {
		return String.format( "File: %S, genre: %s", imageFile.getName(), genreName );
	}

	@Override
	public int hashCode() {
		return 31 * genreName.hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( this == obj ) {
			return true;
		}

		if ( obj == null ) {
			return false;
		}

		if ( ! this.getClass().equals( obj.getClass() ) ) {
			return false;
		}

		final ImageToImport imageToImport = ( ImageToImport ) obj;
		return imageFile == imageToImport.getImageFile() && photosImportSource == imageToImport.getPhotosImportSource() && genreName.equals( imageToImport.getGenreName() );
	}
}
