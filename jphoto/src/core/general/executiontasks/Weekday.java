package core.general.executiontasks;

import core.interfaces.Identifiable;
import utils.StringUtilities;

import java.util.Calendar;

public enum Weekday implements Identifiable {
	MONDAY( Calendar.MONDAY, "monday", "MON" )
	, TUESDAY( Calendar.TUESDAY, "tuesday", "TUE" )
	, WEDNESDAY( Calendar.WEDNESDAY, "wednesday", "WED" )
	, THURSDAY( Calendar.THURSDAY, "thursday", "THU" )
	, FRIDAY( Calendar.FRIDAY, "friday", "FRI" )
	, SATURDAY( Calendar.SATURDAY, "saturday", "SAT" )
	, SUNDAY( Calendar.SUNDAY, "sunday", "SUN" )
	;

	private final int id;
	private final String name;
	private final String croneSchedulerName;

	private Weekday( final int id, final String name, final String croneSchedulerName ) {
		this.id = id;
		this.name = name;
		this.croneSchedulerName = croneSchedulerName;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCroneSchedulerName() {
		return croneSchedulerName;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
	}

	public static Weekday getById( final int id ) {
		for ( final Weekday weekday : Weekday.values() ) {
			if ( weekday.getId() == id ) {
				return weekday;
			}
		}

		throw new IllegalArgumentException( String.format( "Invalid index %s", id ) );
	}
}
