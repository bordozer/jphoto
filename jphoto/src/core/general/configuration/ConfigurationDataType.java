package core.general.configuration;

import java.util.EnumSet;

public enum ConfigurationDataType {

	INTEGER( 1, "ConfigurationDataType: Int number" )
	, STRING( 2, "ConfigurationDataType: String" )
	, BYTE( 3, "ConfigurationDataType: Bytes" )
	, FLOAT( 4, "ConfigurationDataType: Float number" )
	, ARRAY_OF_STRINGS( 5, "ConfigurationDataType: String array")
	, ARRAY_OF_INTEGERS( 6, "ConfigurationDataType: Integer Array")
	, YES_NO( 7, "ConfigurationDataType: Yes/No")
	;

	private final int id;
	private final String name;

	public final static EnumSet INTEGER_KEYS = EnumSet.of( INTEGER, BYTE );
	public final static EnumSet NUMERIC_KEYS = EnumSet.of( INTEGER, BYTE, FLOAT );

	private ConfigurationDataType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static ConfigurationDataType getById( final int id ) {
		for ( final ConfigurationDataType configurationDataType : ConfigurationDataType.values() ) {
			if ( configurationDataType.getId() == id ) {
				return configurationDataType;
			}
		}
		return null;
	}
}
