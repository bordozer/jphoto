package core.general.configuration;

public enum ConfigurationUnit {

	EMPTY( 0, "" )
	, ITEM( 1, "ConfigurationUnit: Items" )
	, DAY( 2, "ConfigurationUnit: Days" )
	, MARK( 3, "ConfigurationUnit: Marks" )
	, SYMBOL( 4, "ConfigurationUnit: Symbols" )
	, BYTE( 5, "ConfigurationUnit: Bytes" )
	, PHOTO( 6, "ConfigurationUnit: Photos" )
	, MIN( 7, "ConfigurationUnit: Minutes" )
	, SECOND( 8, "ConfigurationUnit: Seconds" )
	, MILLISECOND( 8, "ConfigurationUnit: Milliseconds" )
	, VOICE( 9, "ConfigurationUnit: Voices" )
	, KILOBYTE( 10, "ConfigurationUnit: Kb" )
	, MEGABYTE( 11, "ConfigurationUnit: Mb" )
	, PIXEL( 12, "ConfigurationUnit: Pixel" )
	, RANK( 13, "ConfigurationUnit: Rank" )
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

	public static ConfigurationUnit getById( final int id ) {
		for ( final ConfigurationUnit configurationDataType : ConfigurationUnit.values() ) {
			if ( configurationDataType.getId() == id ) {
				return configurationDataType;
			}
		}
		return null;
	}
}
