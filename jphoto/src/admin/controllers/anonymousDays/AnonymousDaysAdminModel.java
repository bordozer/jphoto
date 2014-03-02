package admin.controllers.anonymousDays;

import core.general.anonym.AnonymousDay;
import core.general.base.AbstractGeneralModel;

import java.util.Date;
import java.util.List;

public class AnonymousDaysAdminModel extends AbstractGeneralModel {

	private List<AnonymousDay> anonymousDays;
	private int anonymousPeriod;

	private int anonymousDaysForYear;
	private int currentYear;

	private Date calendarMinDate;
	private Date calendarMaxDate;

	public List<AnonymousDay> getAnonymousDays() {
		return anonymousDays;
	}

	public void setAnonymousDays( final List<AnonymousDay> anonymousDays ) {
		this.anonymousDays = anonymousDays;
	}

	public int getAnonymousPeriod() {
		return anonymousPeriod;
	}

	public void setAnonymousPeriod( final int anonymousPeriod ) {
		this.anonymousPeriod = anonymousPeriod;
	}

	public int getAnonymousDaysForYear() {
		return anonymousDaysForYear;
	}

	public void setAnonymousDaysForYear( final int anonymousDaysForYear ) {
		this.anonymousDaysForYear = anonymousDaysForYear;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear( final int currentYear ) {
		this.currentYear = currentYear;
	}

	public Date getCalendarMinDate() {
		return calendarMinDate;
	}

	public void setCalendarMinDate( final Date calendarMinDate ) {
		this.calendarMinDate = calendarMinDate;
	}

	public Date getCalendarMaxDate() {
		return calendarMaxDate;
	}

	public void setCalendarMaxDate( final Date calendarMaxDate ) {
		this.calendarMaxDate = calendarMaxDate;
	}
}
