package admin.jobs.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum DateRangeType {

	DATE_RANGE( 1, "date range" )
	, TIME_PERIOD( 2, "time period" )
	, CURRENT_TIME( 3, "actual time" )
	;

	private final int id;
	private final String name;

	private DateRangeType( final int id, final String name ) {
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

	public static DateRangeType getById( final int id ) {
		for ( final DateRangeType dateRangeType : DateRangeType.values() ) {
			if ( dateRangeType.getId() == id ) {
				return dateRangeType;
			}
		}

		throw new IllegalArgumentException( String.format( "Invalid index: %s", id ) );
	}
}
