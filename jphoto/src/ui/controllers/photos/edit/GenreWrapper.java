package ui.controllers.photos.edit;

import core.general.genre.Genre;

// TODO: move somewhere because it is used in /home/blu/dev/src/jphoto/jphoto/src/ui/controllers/photos/list/PhotoListController.java
public class GenreWrapper {

	private final Genre genre;
	private String genreNameTranslated;

	public GenreWrapper( final Genre genre ) {
		this.genre = genre;
	}

	public Genre getGenre() {
		return genre;
	}

	public String getGenreNameTranslated() {
		return genreNameTranslated;
	}

	public void setGenreNameTranslated( final String genreNameTranslated ) {
		this.genreNameTranslated = genreNameTranslated;
	}
}
