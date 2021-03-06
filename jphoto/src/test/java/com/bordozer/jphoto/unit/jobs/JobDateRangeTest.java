package com.bordozer.jphoto.unit.jobs;

import com.bordozer.jphoto.admin.jobs.enums.DateRangeType;
import com.bordozer.jphoto.admin.jobs.general.JobDateRange;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class JobDateRangeTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void dateRangeNoMinutes() {
        final Date dateFrom = dateUtilsService.parseDate("2013-08-05");
        final Date dateTo = dateUtilsService.parseDate("2013-08-08");

        final JobDateRange jobDateRange = new JobDateRange(DateRangeType.DATE_RANGE, dateFrom, dateTo, 0, dateUtilsService);

        final Date expectedResult = dateUtilsService.parseDateTime("2013-08-05", "00:00:00");
        final Date actualResult = jobDateRange.getStartDate();

        assertEquals("The values are not equal", expectedResult, actualResult);
    }

    @Test
    public void dateRangeWithMinutes() {
        final Date dateFrom = dateUtilsService.parseDateTime("2013-08-05", "12:14:03");
        final Date dateTo = dateUtilsService.parseDateTime("2013-08-08", "12:14:44");

        final JobDateRange jobDateRange = new JobDateRange(DateRangeType.DATE_RANGE, dateFrom, dateTo, 0, dateUtilsService);

        final Date expectedResult = dateUtilsService.parseDateTime("2013-08-05", "00:00:00");
        final Date actualResult = jobDateRange.getStartDate();

        assertEquals("The values are not equal", expectedResult, actualResult);
    }

    @Test
    public void dateRangeTimePeriodDoesNotMatter() {
        final Date dateFrom = dateUtilsService.parseDateTime("2013-08-05", "12:14:03");
        final Date dateTo = dateUtilsService.parseDateTime("2013-08-08", "12:14:44");

        final JobDateRange jobDateRange = new JobDateRange(DateRangeType.DATE_RANGE, dateFrom, dateTo, 10, dateUtilsService); // Time period '10' is ignored

        final Date expectedResult = dateUtilsService.parseDateTime("2013-08-05", "00:00:00");
        final Date actualResult = jobDateRange.getStartDate();

        assertEquals("The values are not equal", expectedResult, actualResult);
    }
}
