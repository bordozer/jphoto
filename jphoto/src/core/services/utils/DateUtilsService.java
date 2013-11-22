package core.services.utils;

import core.general.data.TimeRange;

import java.util.Date;

public interface DateUtilsService {

	String BEAN_NAME = "dateUtilsService";

	String formatDate( final Date date );

	String formatTime( final Date time );

	String formatTimeShort( final Date time );

	String formatDateTime( final Date time );

	String formatDateTime( final Date time, final String format );

	String formatDateTimeShort( final Date time );

	Date extractDate( final Date time );

	Date getDatesOffset( final Date date, final int offsetInDays );

	Date getTimeOffsetInSeconds( final Date date, final int offsetInSeconds );

	Date getTimeOffsetInMinutes( final Date date, final int offsetInMinutes );

	Date getDatesOffsetFromCurrentDate( final int offsetInDays );

	Date getMonthsOffset( final Date date, final int offsetInMonth );

	Date getMonthsOffsetFromCurrentDate( final int offsetInMonth );

	Date getYearsOffset( final Date date, final int offsetInYears );

	Date getYearsOffsetFromCurrentDate( final int offsetInYear );

	Date parseDate( final String strDate );

	Date parseDateTime( final String strDate );

	boolean validateDate( final String strDate );

	boolean validateTime( final String strTime );

	boolean validateTimeShort( final String strTime );

	Date parseTimeShort( final String strTime );

	Date parseDateTime( final String strDate, final String strTime );

	Date parseDateTimeWithFormat( final String strDateTime, final String format );

	Date parseDateTime( final String strDate, final String strTime, final String format );

	Date getFirstSecondOfDay( final Date time );

	Date getFirstSecondOfToday();

	Date getLastSecondOfToday();

	Date getLastSecondOfDay( final Date time );

	Date getFirstSecondOfTheDayNDaysAgo( final int daysAgo );

	Date getCurrentDate();

	Date getCurrentTime();

	Date getCurrentTimeShort();

	Date getFirstSecondOfLastMonday( final Date time );

	Date getFirstSecondOfLastMonday();

	Date getLastSecondOfNextSunday( final Date time );

	Date getLastSecondOfNextSunday();

	Date getFirstSecondOfNextMonday();

	int getDay( final Date time );

	int getMonth( final Date time );

	int getYear( final Date time );

	Date getFirstSecondOfMonth( final Date time );

	Date getFirstSecondOfMonth();

	Date getLastSecondOfMonth( final Date time );

	Date getLastSecondOfMonth();

	int getWeekNumber( final Date time );

	int getDifferenceInDays( final Date dateFrom, final Date dateTo );

	int getDifferenceInSeconds( final Date timeFrom, final Date timeTo );

	long getDifferenceInMilliseconds( final Date timeFrom, final Date timeTo );

	Date getTimeBetween( final Date timeFrom, final Date timeTo );

	TimeRange getTimeRangeToday();

	TimeRange getTimeRangeFullDays( final Date timeFrom, final Date timeTo );

	Date getFirstSecondOfTomorrow();

	Date getFirstSecondOfYear( final int year );

	Date getLastSecondOfYear( final int year );

	Date parseTime( final String time );

	Date parseTime( final String time, final String format );

	Date getEmptyTime();

	boolean isEmptyTime( final Date time );

	boolean isNotEmptyTime( final Date time );
}
