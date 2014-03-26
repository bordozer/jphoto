package admin.controllers.genres.translations;

import core.services.translator.Language;

public class GenreTranslationEntry {

	private Language language;
	private String value;

	public GenreTranslationEntry() {
	}

	public GenreTranslationEntry( final Language language, final String value ) {
		this.language = language;
		this.value = value;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue( final String value ) {
		this.value = value;
	}
}
