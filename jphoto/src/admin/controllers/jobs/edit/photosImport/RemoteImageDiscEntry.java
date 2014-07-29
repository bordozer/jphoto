package admin.controllers.jobs.edit.photosImport;

import java.io.File;

public class RemoteImageDiscEntry {

	private final GenreDiscEntry genreDiscEntry;
	private final File imageFile;

	public RemoteImageDiscEntry( final File imageFile, final GenreDiscEntry genreDiscEntry ) {
		this.imageFile = imageFile;
		this.genreDiscEntry = genreDiscEntry;
	}

	public GenreDiscEntry getGenreDiscEntry() {
		return genreDiscEntry;
	}

	public File getImageFile() {
		return imageFile;
	}

	@Override
	public String toString() {
		return String.format( "File: %S, genre: %s", imageFile.getName(), genreDiscEntry.getName() );
	}

	@Override
	public int hashCode() {
		return 31 * genreDiscEntry.hashCode();
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

		final RemoteImageDiscEntry remoteImageDiscEntry = ( RemoteImageDiscEntry ) obj;
		return imageFile == remoteImageDiscEntry.getImageFile() && genreDiscEntry == remoteImageDiscEntry.getGenreDiscEntry();
	}
}
