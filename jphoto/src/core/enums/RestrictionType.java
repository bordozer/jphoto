package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum RestrictionType {
	USER_LOGIN( 1, "Login in" )
	, USER_PHOTO_UPLOADING( 2, "Photo uploading" )
	, USER_COMMENTING( 3, "Commenting" )
	, PHOTO_BE_PHOTO_OF_THE_DAY( 4, "Be photo of the day" )
	;

	private final int id;
	private final String name;

	private RestrictionType( final int id, final String name ) {
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

	public static RestrictionType getById( final int id ) {
		for ( final RestrictionType restrictionType : RestrictionType.values() ) {
			if ( restrictionType.getId() == id ) {
				return restrictionType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal RestrictionType id: %d", id ) );
	}
}
