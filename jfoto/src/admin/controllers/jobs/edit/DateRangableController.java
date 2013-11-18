package admin.controllers.jobs.edit;

import admin.jobs.entries.AbstractDateRangeableJob;
import admin.jobs.enums.DateRangeType;
import admin.jobs.general.JobDateRange;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import utils.NumberUtils;

import java.util.Date;
import java.util.Map;

public abstract class DateRangableController extends AbstractJobController {

	public void prepareDateRange( final DateRangableModel model ) {

		final Date firstPhotoUploadDate = dateUtilsService.getFirstSecondOfDay( jobHelperService.getFirstPhotoUploadTime() );
		final Date lastSecondOfToday = dateUtilsService.getLastSecondOfToday();

		model.setDateFrom( dateUtilsService.formatDate( firstPhotoUploadDate ) );
		model.setDateTo( dateUtilsService.formatDate( lastSecondOfToday ) );

		final int days = dateUtilsService.getDifferenceInDays( firstPhotoUploadDate, lastSecondOfToday );
		model.setTimePeriod( String.valueOf( days ) );

		model.setDateRangeTypeId( String.valueOf( DateRangeType.TIME_PERIOD.getId() ) );
	}

	protected void initModelDateRange( final DateRangableModel aModel, final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap ) {

		final int dateRangeTypeId = savedJobParametersMap.get( SavedJobParameterKey.PARAM_DATE_RANGE_TYPE_ID ).getValueInt();
		aModel.setDateRangeTypeId( String.valueOf( dateRangeTypeId ) );

		final DateRangeType dateRangeType = DateRangeType.getById( dateRangeTypeId );

		switch ( dateRangeType ) {
			case DATE_RANGE:
				final Date dateFrom1 = savedJobParametersMap.get( SavedJobParameterKey.PARAM_DATE_FROM ).getValueTime( dateUtilsService );
				final Date dateTo1 = savedJobParametersMap.get( SavedJobParameterKey.PARAM_DATE_TO ).getValueTime( dateUtilsService );
				aModel.setDateFrom( dateUtilsService.formatDate( dateFrom1 ) );
				aModel.setDateTo( dateUtilsService.formatDate( dateTo1 ) );
				final int timePeriod1 = dateUtilsService.getDifferenceInDays( dateFrom1, dateTo1 );
				aModel.setTimePeriod( String.valueOf( timePeriod1 ) );

				break;
			case TIME_PERIOD:
				final int timePeriod2 = savedJobParametersMap.get( SavedJobParameterKey.PARAM_TIME_PERIOD ).getValueInt();
				aModel.setTimePeriod( String.valueOf( timePeriod2 ) );

				final Date dateFrom2 = dateUtilsService.getFirstSecondOfTheDayNDaysAgo( timePeriod2 );
				final Date dateTo2 = dateUtilsService.getCurrentTime();
				aModel.setDateFrom( dateUtilsService.formatDate( dateFrom2 ) );
				aModel.setDateTo( dateUtilsService.formatDate( dateTo2 ) );

				break;
			case CURRENT_TIME:
				aModel.setTimePeriod( "0" );

				final Date currentTime = dateUtilsService.getCurrentTime();
				aModel.setDateFrom( dateUtilsService.formatDate( currentTime ) );
				aModel.setDateTo( dateUtilsService.formatDate( currentTime ) );

				break;
			default:
				throw new IllegalArgumentException( String.format( "Illegal DateRangeType: %s", dateRangeType ) );
		}
	}

	protected void initDateRangeJob( final DateRangableModel model, final AbstractDateRangeableJob job ) {

		final String dateRangeTypeId = model.getDateRangeTypeId();
		final DateRangeType dateRangeType = DateRangeType.getById( Integer.parseInt( dateRangeTypeId ) );

		final Date dateFrom = dateUtilsService.parseDate( model.getDateFrom() );
		final Date dateTo = dateUtilsService.parseDate( model.getDateTo() );

		final int timePeriod = NumberUtils.convertToInt( model.getTimePeriod() );

		final JobDateRange jobDateRangeTemp = new JobDateRange( dateRangeType, dateFrom, dateTo, timePeriod, dateUtilsService );

		job.setJobDateRange( jobDateRangeTemp );
	}
}
