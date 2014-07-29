package admin.controllers.jobs.edit.photosImport;

import java.io.File;

public class ImageToImport {

	private PhotosImportSource photosImportSource;
	private final String genreName;
	private final File imageFile;

	public ImageToImport( final PhotosImportSource photosImportSource, final String genreName, final File imageFile ) {
		this.photosImportSource = photosImportSource;
		this.genreName = genreName;
		this.imageFile = imageFile;
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
