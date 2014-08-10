package core.services.utils;

import core.exceptions.BaseRuntimeException;
import core.general.data.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtilsServiceImpl implements DateUtilsService {

	public static final Date EMPTY_TIME = new Date( 0 );
	public static final int ONE_HOUR = 60 * 60 * 1000;

	@Autowired
	private SystemVarsService systemVarsService;
	
	@Override
	public String formatDate( final Date date ) {
		return format( date, systemVarsService.getSystemDateFormat() );
	}

	@Override
	public String formatTime( final Date time ) {
		return format( time, systemVarsService.getSystemTimeFormat() );
	}

	@Override
	public String formatTimeShort( final Date time ) {
		return format( time, systemVarsService.getSystemTimeFormatShort() );
	}

	@Override
	public String formatDateTime( final Date time ) {
		return format( time, getSystemDateTimeFormat() );
	}

	@Override
	public String formatDateTime( final Date time, final String format ) {
		return format( time, format );
	}

	@Override
	public String formatDateTimeShort( final Date time ) {
		return format( time, getSystemDateTimeFormatShort() );
	}

	@Override
	public Date extractDate( final Date time ) {
		final String strDate = formatDate( time );
		return parseDate( strDate );
	}

	@Override
	public Date getDatesOffset( final Date date, final int offsetInDays ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.DATE, offsetInDays );

		return cal.getTime();
	}

	@Override
	public Date getTimeOffsetInSeconds( final Date date, final int offsetInSeconds ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.SECOND, offsetInSeconds );

		return cal.getTime();
	}

	@Override
	public Date getTimeOffsetInHours( final Date date, final int offsetInHours ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.HOUR, offsetInHours );

		return cal.getTime();
	}

	@Override
	public Date getTimeOffsetInMonth( final Date date, final int offsetInMonth ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.MONTH, offsetInMonth );

		return cal.getTime();
	}

	@Override
	public Date getTimeOffsetInYear( final Date date, final int offsetInYear ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.YEAR, offsetInYear );

		return cal.getTime();
	}

	@Override
	public Date getTimeOffsetInMinutes( final Date date, final int offsetInMinutes ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.MINUTE, offsetInMinutes );

		return cal.getTime();
	}

	@Override
	public Date getDatesOffsetFromCurrentDate( final int offsetInDays ) {
		return getDatesOffset( new Date(), offsetInDays );
	}

	@Override
	public Date getMonthsOffset( final Date date, final int offsetInMonth ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.MONTH, offsetInMonth );

		return cal.getTime();
	}

	@Override
	public Date getMonthsOffsetFromCurrentDate( final int offsetInMonth ) {
		return getMonthsOffset( new Date(), offsetInMonth );
	}

	@Override
	public Date getYearsOffset( final Date date, final int offsetInYears ) {
		final Calendar cal = getCalendar( date );
		cal.add( Calendar.YEAR, offsetInYears );

		return cal.getTime();
	}

	@Override
	public Date getYearsOffsetFromCurrentDate( final int offsetInYear ) {
		return getYearsOffset( new Date(), offsetInYear );
	}

	@Override
	public Date parseDate( final String strDate ) {
		final SimpleDateFormat sdf = getFormat( systemVarsService.getSystemDateFormat() );
		try {
			return sdf.parse( strDate );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public Date parseDateTime( final String strDate ) {
		final SimpleDateFormat sdf = getFormat( getSystemDateTimeFormat() );
		try {
			return sdf.parse( strDate );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public boolean validateDate( final String strDate ) {
		final SimpleDateFormat sdf = getFormat( systemVarsService.getSystemDateFormat() );
		try {
			sdf.parse( strDate );
			return true;
		} catch ( ParseException e ) {
			return false;
		}
	}

	@Override
	public boolean validateTime( final String strTime ) {
		try {
			parseTime( strTime );
			return true;
		} catch ( Throwable e ) {
			return false;
		}
	}

	@Override
	public boolean validateTimeShort( final String strTime ) {
		return validateTime( String.format( "%s:00", strTime ) );
	}

	@Override
	public Date parseTimeShort( final String strTime ) {
		final SimpleDateFormat sdf = getFormat( systemVarsService.getSystemTimeFormat() );
		try {
			return sdf.parse( String.format( "%s:00", strTime ) );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public Date parseDateTime( final String strDate, final String strTime ) {
		return parseDateTime( strDate, strTime, String.format( "%s %s", systemVarsService.getSystemDateFormat(), systemVarsService.getSystemTimeFormat() ) );
	}

	@Override
	public Date parseDateTimeWithFormat( final String strDateTime, final String format ) {
		final SimpleDateFormat sdf = getFormat( format );
		try {
			return sdf.parse( strDateTime );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public Date parseDateTime( final String strDate, final String strTime, final String format ) {
		final SimpleDateFormat sdf = getFormat( format );
		final String strDateTime = String.format( "%s %s", strDate, strTime );
		try {
			return sdf.parse( strDateTime );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public Date getFirstSecondOfDay( final Date time ) {
		return extractDate( time );
	}

	@Override
	public Date getFirstSecondOfToday() {
		return extractDate( getCurrentTime() );
	}

	@Override
	public Date getLastSecondOfToday() {
		return getLastSecondOfDay( getCurrentTime() );
	}

	@Override
	public Date getLastSecondOfDay( final Date time ) {
		final Date zeroSecondOfDay = extractDate( time );
		final Date zeroSecondOdTheNextDay = getDatesOffset( zeroSecondOfDay, 1 );
		return new Date( zeroSecondOdTheNextDay.getTime() - 1 );
	}

	@Override
	public Date getFirstSecondOfTheDayNDaysAgo( final int daysAgo ) {
		return getFirstSecondOfDay( getDatesOffsetFromCurrentDate( -daysAgo ) );
	}

	@Override
	public Date getCurrentDate() {
		return getFirstSecondOfDay( getCurrentTime() );
	}

	@Override
	public Date getCurrentTimeShort() {
		final Date currentTimeShort = parseTimeShort( formatTimeShort( getCurrentTime() ) );

		return new Date( getCurrentDate().getTime() + currentTimeShort.getTime() );
	}

	@Override
	public Date getFirstSecondOfLastMonday( final Date time ) {
		final Calendar cal = getCalendar( time );
		cal.setFirstDayOfWeek( Calendar.MONDAY );
		while ( cal.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
			cal.add( Calendar.DATE, -1 );
		}

		return getFirstSecondOfDay( cal.getTime() );
	}

	@Override
	public Date getFirstSecondOfLastMonday() {
		return getFirstSecondOfLastMonday( new Date() );
	}

	@Override
	public Date getLastSecondOfNextSunday( final Date time ) {
		return getLastSecondOfNextDayOfWeek( time, Calendar.SUNDAY );
	}

	@Override
	public Date getLastSecondOfNextSunday() {
		return getLastSecondOfNextSunday( new Date() );
	}

	@Override
	public Date getFirstSecondOfNextMonday() {
		return getLastSecondOfNextDayOfWeek( new Date(), Calendar.MONDAY );
	}

	@Override
	public int getDay( final Date time ) {
		return getCalendar( time ).get( Calendar.DAY_OF_MONTH );
	}

	@Override
	public int getMonth( final Date time ) {
		final Calendar calendar = getCalendar( time );
		return calendar.get( Calendar.MONTH );
	}

	@Override
	public int getYear( final Date time ) {
		final Calendar calendar = getCalendar( time );
		return calendar.get( Calendar.YEAR );
	}

	@Override
	public Date getFirstSecondOfMonth( final Date time ) {
		final Calendar calendar = getCalendar( time );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );

		return getFirstSecondOfDay( calendar.getTime() );
	}

	@Override
	public Date getFirstSecondOfMonth() {
		return getFirstSecondOfMonth( new Date() );
	}

	@Override
	public Date getLastSecondOfMonth( final Date time ) {
		final Calendar calendar = getCalendar( time );
		calendar.set( Calendar.YEAR, getYear( time ) );
		calendar.set( Calendar.MONTH, getMonth( time ) );
		calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

		return getLastSecondOfDay( calendar.getTime() );
	}

	@Override
	public Date getLastSecondOfMonth() {
		return getLastSecondOfMonth( new Date() );
	}

	@Override
	public int getWeekNumber( final Date time ) {
		final Calendar calendar = getCalendar( time );
		return calendar.get( Calendar.WEEK_OF_YEAR );
	}

	@Override
	public int getDifferenceInDays( final Date dateFrom, final Date dateTo ) {
		final long diffTime = dateTo.getTime() - dateFrom.getTime();
		return ( int ) ( diffTime / ( 1000 * 60 * 60 * 24 ) );
	}

	@Override
	public int getDifferenceInHours( final Date timeFrom, final Date timeTo ) {
		final long diffTime = timeTo.getTime() - timeFrom.getTime();
		return ( int ) ( diffTime / ( 1000 * 60 * 60 ) );
	}

	@Override
	public int getDifferenceInSeconds( final Date timeFrom, final Date timeTo ) {
		return ( int ) ( getDifferenceInMilliseconds( timeFrom, timeTo ) / 1000 );
	}

	@Override
	public long getDifferenceInMilliseconds( final Date timeFrom, final Date timeTo ) {
		return timeTo.getTime() - timeFrom.getTime();
	}

	@Override
	public Date getTimeBetween( final Date timeFrom, final Date timeTo ) {
		final Calendar calendar = getCalendar();
		final long diff = timeFrom.getTime() - timeTo.getTime();
		calendar.setTimeInMillis( diff - TimeZone.getDefault().getRawOffset() - ONE_HOUR ); // TODO: ONE_HOUR is a hack
		return calendar.getTime();
	}

	@Override
	public TimeRange getTimeRangeToday() {
		final Date currentTime = getCurrentTime();
		return getTimeRangeFullDays( currentTime, currentTime );
	}

	@Override
	public TimeRange getTimeRangeFullDays( final Date timeFrom, final Date timeTo ) {
		final Date theFirstSecondOfTheDay = getFirstSecondOfDay( timeFrom );
		final Date theLastSecondOfTheDay = getLastSecondOfDay( timeTo );

		return new TimeRange( theFirstSecondOfTheDay, theLastSecondOfTheDay );
	}

	@Override
	public Date getFirstSecondOfTomorrow() {
		return getFirstSecondOfDay( getDatesOffset( getCurrentDate(), 1 ) );
	}

	@Override
	public Date getFirstSecondOfYear( final int year ) {
		final Calendar calendar = getCalendar();
		calendar.set( Calendar.YEAR, year );
		calendar.set( Calendar.MONTH, Calendar.JANUARY );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );

		return getFirstSecondOfDay( calendar.getTime() );
	}

	@Override
	public Date getLastSecondOfYear( final int year ) {
		final Calendar calendar = getCalendar();
		calendar.set( Calendar.YEAR, year );
		calendar.set( Calendar.MONTH, Calendar.DECEMBER );
		calendar.set( Calendar.DAY_OF_MONTH, 31 );

		return getLastSecondOfDay( calendar.getTime() );
	}

	@Override
	public Date parseTime( final String time ) {
		return parseTime( time, systemVarsService.getSystemTimeFormat() );
	}

	@Override
	public Date parseTime( final String time, final String format ) {
		final SimpleDateFormat sdf = getFormat( format );
		try {
			return sdf.parse( time );
		} catch ( ParseException e ) {
			throw new BaseRuntimeException( e );
		}
	}

	@Override
	public Date getEmptyTime() {
		return EMPTY_TIME;
	}

	@Override
	public boolean isEmptyTime( final Date time ) {
		return time == null || getEmptyTime().getTime() == time.getTime();
	}

	@Override
	public boolean isNotEmptyTime( final Date time ) {
		return ! isEmptyTime( time );
	}

	@Override
	public boolean isItToday( final Date time ) {
		return getFirstSecondOfDay( time ).getTime() == getFirstSecondOfToday().getTime();
	}

	@Override
	public Date getCurrentTime() {
		return new Date();
	}

	private Calendar getCalendar() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek( Calendar.MONDAY );

		return calendar;
	}

	private Calendar getCalendar( final Date date ) {
		final Calendar calendar = getCalendar();
		calendar.setTime( date );

		return calendar;
	}

	private Date getLastSecondOfNextDayOfWeek( final Date time, final int dayOfWeek ) {
		final Calendar calendar = getCalendar( time );
		final int weekday = calendar.get( Calendar.DAY_OF_WEEK );
		int days = dayOfWeek - weekday;
		if ( days < 0 ) {
			days += 7;
		}
		calendar.add( Calendar.DAY_OF_YEAR, days );

		return getLastSecondOfDay( calendar.getTime() );
	}

	private String getSystemDateTimeFormat() {
		return String.format( "%s %s", systemVarsService.getSystemDateFormat(), systemVarsService.getSystemTimeFormat() );
	}

	private String getSystemDateTimeFormatShort() {
		return String.format( "%s %s", systemVarsService.getSystemDateFormat(), systemVarsService.getSystemTimeFormatShort() );
	}

	private SimpleDateFormat getFormat( final String format ) {
		return getFormat( format, TimeZone.getDefault() );
	}

	private String format( final Date time, final String format ) {
		return getFormat( format ).format( time );
	}

	private SimpleDateFormat getFormat( final String format, final TimeZone timeZone ) {
		final SimpleDateFormat sdf = new SimpleDateFormat( format );
		sdf.setTimeZone( timeZone );

		return sdf;
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}
}
