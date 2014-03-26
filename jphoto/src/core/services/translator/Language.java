package core.services.translator;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum Language {

	NERD( 1, "nerd", "Nerd" )
	, RU( 3, "ru", "Russian" )
	, EN( 2, "en", "English" )
	;

	private final int id;
	private final String code;
	private final String name;

	private Language( final int id, final String code, final String name ) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Language getById( final int id ) {
		for ( final Language language : values() ) {
			if ( language.getId() == id ) {
				return language;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal language id: %d", id ) );
	}

	public static Language getByCode( final String code ) {
		for ( final Language language : values() ) {
			if ( language.getCode().equalsIgnoreCase( code ) ) {
				return language;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal language code: %s", code ) );
	}

	public static List<Language> getUILanguages() {

		final List<Language> languages = newArrayList();

		for ( final Language language : values() ) {
			if ( language != NERD ) {
				languages.add( language );
			}
		}

		return languages;
	}
}
