package core.general.executiontasks;

import core.interfaces.Identifiable;
import utils.StringUtilities;
import utils.TranslatorUtils;

import java.util.Calendar;

public enum Month implements Identifiable {

	JANUARY( Calendar.JANUARY, "january", "JAN" )
	, FEBRUARY( Calendar.FEBRUARY, "february", "FEB" )
	, MARCH( Calendar.MARCH, "march", "MAR" )
	, APRIL( Calendar.APRIL, "april", "APR" )
	, MAY( Calendar.MAY, "may", "MAY" )
	, JUNE( Calendar.JUNE, "june", "JUN" )
	, JULY( Calendar.JULY, "july", "JUL" )
	, AUGUST( Calendar.AUGUST, "august", "AUG" )
	, SEPTEMBER( Calendar.SEPTEMBER, "september", "SEP" )
	, OCTOBER( Calendar.OCTOBER, "october", "OCT" )
	, NOVEMBER( Calendar.NOVEMBER, "november", "NOV" )
	, DECEMBER( Calendar.DECEMBER, "december", "DEC" )
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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( name ); // TODO: translate
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
