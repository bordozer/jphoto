package admin.jobs.general;

import admin.jobs.enums.DateRangeType;
import core.services.utils.DateUtilsService;

import java.util.Date;

public class JobDateRange {

	private final DateRangeType dateRangeType;

	private final Date dateFrom;
	private final Date dateTo;

	private final int timePeriod;

	private final DateUtilsService dateUtilsService;

	public JobDateRange( final DateRangeType dateRangeType, final Date dateFrom, final Date dateTo, final int timePeriod, final DateUtilsService dateUtilsService ) {
		this.dateRangeType = dateRangeType;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.timePeriod = timePeriod;

		this.dateUtilsService = dateUtilsService; // TODO
	}

	public DateRangeType getDateRangeType() {
		return dateRangeType;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public int getTimePeriod() {
		return timePeriod;
	}

	public Date getStartDate() {
		if ( dateRangeType == DateRangeType.CURRENT_TIME ) {
			return dateUtilsService.getCurrentTime();
		}

		if ( dateRangeType == DateRangeType.DATE_RANGE ) {
			return dateUtilsService.getFirstSecondOfDay( dateFrom );
		}

		return dateUtilsService.getFirstSecondOfTheDayNDaysAgo( timePeriod );
	}

	public Date getEndDate() {
		if ( dateRangeType == DateRangeType.CURRENT_TIME ) {
			return dateUtilsService.getCurrentTime();
		}

		if ( dateRangeType == DateRangeType.DATE_RANGE ) {
			return dateUtilsService.getLastSecondOfDay( dateTo );
		}

		return dateUtilsService.getLastSecondOfToday();
	}
}
