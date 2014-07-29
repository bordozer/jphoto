package admin.controllers.jobs.edit.photosImport;

import java.io.File;

public class ImportedImage {

	private final String genreName;
	private final File imageFile;

	public ImportedImage( final File imageFile, final String genreName ) {
		this.imageFile = imageFile;
		this.genreName = genreName;
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

		final ImportedImage importedImage = ( ImportedImage ) obj;
		return imageFile == importedImage.getImageFile() && genreName.equals( importedImage.getGenreName() );
	}
}
