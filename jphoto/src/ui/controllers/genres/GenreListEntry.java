package ui.controllers.genres;

import core.general.genre.Genre;

public class GenreListEntry {

	private final Genre genre;
	private String genreNameTranslated;

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
}
