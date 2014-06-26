package admin.jobs.enums;

import core.interfaces.IdentifiableNameable;

public enum DateRangeType implements IdentifiableNameable {

	DATE_RANGE( 1, "DateRangeType: date range" )
	, TIME_PERIOD( 2, "DateRangeType: time period" )
	, CURRENT_TIME( 3, "DateRangeType: actual time" )
	;

	private final int id;
	private final String name;

	private DateRangeType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
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
