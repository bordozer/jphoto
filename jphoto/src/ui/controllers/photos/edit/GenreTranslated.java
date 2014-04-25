package ui.controllers.photos.edit;

import core.general.genre.Genre;

public class GenreTranslated {

	private final Genre genre;
	private final String nameTranslated;

	public GenreTranslated( final Genre genre, final String nameTranslated ) {
		this.genre = genre;
		this.nameTranslated = nameTranslated;
	}

	public Genre getGenre() {
		return genre;
	}

	public String getNameTranslated() {
		return nameTranslated;
	}
}
