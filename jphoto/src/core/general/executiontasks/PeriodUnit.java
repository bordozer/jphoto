package core.general.executiontasks;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum PeriodUnit {

	SECOND( 1, "second" )
	, MINUTE( 2, "minute" )
	, HOUR( 3, "hour" )
//	, DAY( 4, "day" )
	;

	private final int id;
	private final String name;

	private PeriodUnit( final int id, final String name ) {
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

	public static PeriodUnit getById( final int id ) {
		for ( final PeriodUnit configurationDataType : PeriodUnit.values() ) {
			if ( configurationDataType.getId() == id ) {
				return configurationDataType;
			}
		}
		return null;
	}
}
