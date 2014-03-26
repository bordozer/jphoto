package admin.controllers.genres.translations;

import core.services.translator.Language;

public class GenreTranslationEntry {

	private Language language;
	private int entryId;
	private String translation;

	public GenreTranslationEntry() {
	}

	public GenreTranslationEntry( final int entryId, final Language language, final String translation ) {
		this.language = language;
		this.translation = translation;
		this.entryId = entryId;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId( final int entryId ) {
		this.entryId = entryId;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation( final String translation ) {
		this.translation = translation;
	}
}
