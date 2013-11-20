package core.general.configuration;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum ConfigurationDataType {

	INTEGER( 1, "Int number" )
	, STRING( 2, "String" )
	, BYTE( 3, "Bytes" )
	, FLOAT( 4, "Float number" )
	, ARRAY_OF_STRINGS( 5, "String array")
	, ARRAY_OF_INTEGERS( 6, "Integer Array")
	, YES_NO( 7, "Yes/No")
	;

	private final int id;
	private final String name;

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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( TranslatorUtils.translate( name ) );
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
