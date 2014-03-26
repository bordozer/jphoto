package admin.controllers.genres.translations;

import core.services.translator.Language;

public class IdLanguageKey {

	private final int id;
	private final Language language;


	public IdLanguageKey( final int id, final Language language ) {
		this.id = id;
		this.language = language;
	}

	public int getId() {
		return id;
	}

	public Language getLanguage() {
		return language;
	}

	@Override
	public int hashCode() {
		return id * language.hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof IdLanguageKey ) ) {
			return false;
		}

		final IdLanguageKey key = ( IdLanguageKey ) obj;
		return key.getId() == getId() && language == key.getLanguage();
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", id, language );
	}
}
