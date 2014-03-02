package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum UserGender {

	MALE( 1, "male" )
	, FEMALE( 2, "female" )
	;

	private final int id;
	private final String name;

	private UserGender( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
	}

	public static UserGender getById( final int id ) {
		for ( final UserGender upgradeTaskResult : UserGender.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal UserGender id: %d", id ) );
	}
}
