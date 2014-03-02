package core.general.configuration;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum ConfigurationUnit {

	EMPTY( 0, "" )
	, ITEM( 1, "Items" )
	, DAY( 2, "Days" )
	, MARK( 3, "Marks" )
	, SYMBOL( 4, "Symbols" )
	, BYTE( 5, "Bytes" )
	, PHOTO( 6, "Photos" )
	, MIN( 7, "Minutes" )
	, SECOND( 8, "Seconds" )
	, MILLISECOND( 8, "Milliseconds" )
	, VOICE( 9, "Voices" )
	, KILOBYTE( 10, "Kb" )
	, MEGABYTE( 11, "Mb" )
	, PIXEL( 12, "Pixel" )
	, RANK( 13, "Rank" )
	;

	private final int id;
	private final String name;

	private ConfigurationUnit( final int id, final String name ) {
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

	public static ConfigurationUnit getById( final int id ) {
		for ( final ConfigurationUnit configurationDataType : ConfigurationUnit.values() ) {
			if ( configurationDataType.getId() == id ) {
				return configurationDataType;
			}
		}
		return null;
	}
}
