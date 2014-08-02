package ui.controllers.genres;

import core.general.genre.Genre;
import core.general.photo.PhotoPreviewWrapper;

public class GenreListEntry {

	private final Genre genre;
	private String genreNameTranslated;
	private PhotoPreviewWrapper photoPreviewWrapper;
	private String photosByGenreURL;

	public GenreListEntry( final Genre genre ) {
		this.genre = genre;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenreNameTranslated( final String genreNameTranslated ) {
		this.genreNameTranslated = genreNameTranslated;
	}

	public String getGenreNameTranslated() {
		return genreNameTranslated;
	}

	public void setPhotoPreviewWrapper( final PhotoPreviewWrapper photoPreviewWrapper ) {
		this.photoPreviewWrapper = photoPreviewWrapper;
	}

	public PhotoPreviewWrapper getPhotoPreviewWrapper() {
		return photoPreviewWrapper;
	}

	public void setPhotosByGenreURL( final String photosByGenreURL ) {
		this.photosByGenreURL = photosByGenreURL;
	}

	public String getPhotosByGenreURL() {
		return photosByGenreURL;
	}
}
