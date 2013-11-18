package utils;

import common.AbstractTestCase;
import core.general.data.TimeRange;
import core.services.utils.DateUtilsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilsServiceTest extends AbstractTestCase {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void dateOffset() {
		final String dateStr = "2012-12-05";

		final Date date = dateUtilsService.parseDate( dateStr );
		final Date date3DaysAgo = dateUtilsService.getDatesOffset( date, -3 );

		assertEquals( "2012-12-02", dateUtilsService.formatDate( date3DaysAgo ) );
	}

	@Test
	public void dateOffsetAndTimeDoesNotMatter() {
		final String dateStr = "2012-12-05 12:01:14";

		final Date date = dateUtilsService.parseDate( dateStr );
		final Date date3DaysAgo = dateUtilsService.getDatesOffset( date, -3 );

		assertEquals( "2012-12-02", dateUtilsService.formatDate( date3DaysAgo ) );
	}

	@Test
	public void monthOffset() {
		final String dateStr = "2012-12-05";

		final Date date = dateUtilsService.parseDate( dateStr );
		final Date date3MonthAgo = dateUtilsService.getMonthsOffset( date, -3 );

		assertEquals( "2012-09-05", dateUtilsService.formatDate( date3MonthAgo ) );
	}

	@Test
	public void yearsOffset() {
		final String dateStr = "2012-12-05";

		final Date date = dateUtilsService.parseDate( dateStr );
		final Date date3YearsAgo = dateUtilsService.getYearsOffset( date, -3 );

		assertEquals( "2009-12-05", dateUtilsService.formatDate( date3YearsAgo ) );
	}

	@Test
	public void getFirstSecondOfLastMondayOnDate() {
		final Date date = dateUtilsService.parseDate( "2013-01-05" ); // Saturday
		final Date lastMonday = dateUtilsService.getFirstSecondOfLastMonday( date );

		assertEquals( "2012-12-31 00:00:00", dateUtilsService.formatDateTime( lastMonday ) ); // Sunday
	}

	@Test
	public void getFirstSecondOfLastMondayOnDate1() {
		final Date date = dateUtilsService.parseDate( "2013-01-07" ); // Monday
		final Date lastMonday = dateUtilsService.getFirstSecondOfLastMonday( date );

		assertEquals( "2013-01-07 00:00:00", dateUtilsService.formatDateTime( lastMonday ) ); // first second od the same date
	}

	@Test
	public void getFirstSecondOfLastMondayOnDate2() {
		final Date date = dateUtilsService.parseDateTime( "2013-01-14", "12:00:00" ); // Saturday
		final Date lastMonday = dateUtilsService.getFirstSecondOfLastMonday( date );

		assertEquals( "2013-01-14 00:00:00", dateUtilsService.formatDateTime( lastMonday ) ); // Sunday
	}

	@Test
	public void getLastSecondOfSunday() {
		final Date date = dateUtilsService.parseDate( "2013-01-03" ); // Thusday
		final Date nextSunday = dateUtilsService.getLastSecondOfNextSunday( date );

		assertEquals( "2013-01-06 23:59:59", dateUtilsService.formatDateTime( nextSunday ) ); // last second of Sunday
	}

	@Test
	public void getLastSecondOfSunday1() {
		final Date date = dateUtilsService.parseDate( "2013-01-06" ); // Sunday
		final Date nextSunday = dateUtilsService.getLastSecondOfNextSunday( date );

		assertEquals( "2013-01-06 23:59:59", dateUtilsService.formatDateTime( nextSunday ) ); // last second of the same day
	}

	@Test
	public void getWeekNumber() {
		assertEquals( 1, dateUtilsService.getWeekNumber( dateUtilsService.parseDate( "2013-01-02" ) ) ); // Wednsday
		assertEquals( 1, dateUtilsService.getWeekNumber( dateUtilsService.parseDate( "2013-01-06" ) ) ); // Sunday - end of the first eek
		assertEquals( 2, dateUtilsService.getWeekNumber( dateUtilsService.parseDate( "2013-01-07" ) ) ); // Monday - begin of the second week
	}

	@Test
	public void dayMonthYear() {
		assertEquals( 21, dateUtilsService.getDay( dateUtilsService.parseDate( "2013-01-21" ) ) );
		assertEquals( 11, dateUtilsService.getMonth( dateUtilsService.parseDate( "2013-12-21" ) ) ); // JANUARY has index 0!!!!!
		assertEquals( 2013, dateUtilsService.getYear( dateUtilsService.parseDate( "2013-12-21" ) ) );
	}

	@Test
	public void firstLastDaysOfMonth() {
		assertEquals( "2013-01-01 00:00:00", dateUtilsService.formatDateTime( dateUtilsService.getFirstSecondOfMonth( dateUtilsService.parseDate( "2013-01-21" ) ) ) );
		assertEquals( "2013-01-31 23:59:59", dateUtilsService.formatDateTime( dateUtilsService.getLastSecondOfMonth( dateUtilsService.parseDate( "2013-01-21" ) ) ) );
		assertEquals( "2012-02-29 23:59:59", dateUtilsService.formatDateTime( dateUtilsService.getLastSecondOfMonth( dateUtilsService.parseDate( "2012-02-12" ) ) ) );
		assertEquals( "2012-02-29 23:59:59", dateUtilsService.formatDateTime( dateUtilsService.getLastSecondOfMonth( dateUtilsService.parseDate( "2012-02-12 01:12:12" ) ) ) );
	}

	@Test
	public void TimeRange1() {
		final Date timeFrom = dateUtilsService.parseDate( "2013-01-03" );
		final Date timeTo = dateUtilsService.parseDate( "2013-01-10" );
		final TimeRange timeRange = dateUtilsService.getTimeRangeFullDays( timeFrom, timeTo );

		assertEquals( "2013-01-03 00:00:00", dateUtilsService.formatDateTime( timeRange.getTimeFrom() ) );
		assertEquals( "2013-01-10 23:59:59", dateUtilsService.formatDateTime( timeRange.getTimeTo() ) );
	}

	@Test
	public void TimeRange2() {
		final Date timeFrom = dateUtilsService.parseDate( "2013-01-03 11:23" );
		final Date timeTo = dateUtilsService.parseDate( "2013-01-10 23:05" );
		final TimeRange timeRange = dateUtilsService.getTimeRangeFullDays( timeFrom, timeTo );

		assertEquals( "2013-01-03 00:00:00", dateUtilsService.formatDateTime( timeRange.getTimeFrom() ) );
		assertEquals( "2013-01-10 23:59:59", dateUtilsService.formatDateTime( timeRange.getTimeTo() ) );
	}

	@Test
	public void timeOffsetInSeconds() {
		final Date time = dateUtilsService.parseDateTime( "2013-01-03 00:00:00" );

		assertEquals( dateUtilsService.parseDateTime( "2013-01-03 00:00:10" ), dateUtilsService.getTimeOffsetInSeconds( time, 10 ) );
		assertEquals( dateUtilsService.parseDateTime( "2013-01-03 00:01:00" ), dateUtilsService.getTimeOffsetInSeconds( time, 60 ) );
		assertEquals( dateUtilsService.parseDateTime( "2013-01-03 01:01:01" ), dateUtilsService.getTimeOffsetInSeconds( time, 3661 ) );
		assertEquals( dateUtilsService.parseDateTime( "2013-01-03 03:20:00" ), dateUtilsService.getTimeOffsetInSeconds( time, 12000 ) );
	}

	@Test
	public void differenceInSeconds() {
		final Date time = dateUtilsService.parseDateTime( "2013-01-03 00:00:00" );

		assertEquals( 10, dateUtilsService.getDifferenceInSeconds( time, dateUtilsService.parseDateTime( "2013-01-03 00:00:10" ) ) );
		assertEquals( 60, dateUtilsService.getDifferenceInSeconds( time, dateUtilsService.parseDateTime( "2013-01-03 00:01:00" ) ) );
	}
}
