package core.general.executiontasks;

import core.interfaces.IdentifiableNameable;

import java.util.Calendar;

public enum Month implements IdentifiableNameable {

	JANUARY( Calendar.JANUARY, "Month: january", "JAN" )
	, FEBRUARY( Calendar.FEBRUARY, "Month: february", "FEB" )
	, MARCH( Calendar.MARCH, "Month: march", "MAR" )
	, APRIL( Calendar.APRIL, "Month: april", "APR" )
	, MAY( Calendar.MAY, "Month: may", "MAY" )
	, JUNE( Calendar.JUNE, "Month: june", "JUN" )
	, JULY( Calendar.JULY, "Month: july", "JUL" )
	, AUGUST( Calendar.AUGUST, "Month: august", "AUG" )
	, SEPTEMBER( Calendar.SEPTEMBER, "Month: september", "SEP" )
	, OCTOBER( Calendar.OCTOBER, "Month: october", "OCT" )
	, NOVEMBER( Calendar.NOVEMBER, "Month: november", "NOV" )
	, DECEMBER( Calendar.DECEMBER, "Month: december", "DEC" )
	;

	private final int id;
	private final String name;
	private final String croneSchedulerName;

	private Month( final int id, final String name, final String croneSchedulerName ) {
		this.id = id;
		this.name = name;
		this.croneSchedulerName = croneSchedulerName;
	}

	public int getId() {
		return id;
	}

	public String getCroneSchedulerName() {
		return croneSchedulerName;
	}

	public String getName() {
		return name;
	}

	public static Month getById( final int id ) {
		for ( final Month weekday : Month.values() ) {
			if ( weekday.getId() == id ) {
				return weekday;
			}
		}

		throw new IllegalArgumentException( String.format( "Invalid index %s", id ) );
	}
}
