package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.DateRangeType;
import admin.jobs.general.JobDateRange;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.Map;

public abstract class AbstractDateRangeableJob extends AbstractJob {

	protected JobDateRange jobDateRange;

	protected AbstractDateRangeableJob( final LogHelper log, final JobRuntimeEnvironment jobEnvironment ) {
		super( log, jobEnvironment );
	}

	public void addDateRangeParameters( final StringBuilder builder ) {

		final DateRangeType dateRangeType = jobDateRange.getDateRangeType();
		final TranslatorService translatorService = services.getTranslatorService();

		final String dateRangeText = dateRangeType.getNameTranslated();
		final String timePeriodText = translatorService.translate( "$1 $2 days", jobEnvironment.getLanguage(), dateRangeType.getNameTranslated(), String.valueOf( jobDateRange.getTimePeriod() ) );
		final String currentTimeText = translatorService.translate( "Time: $1", jobEnvironment.getLanguage(), DateRangeType.CURRENT_TIME.getNameTranslated() );

		final String dateRange = dateRangeType == DateRangeType.DATE_RANGE ? dateRangeText : dateRangeType == DateRangeType.TIME_PERIOD ? timePeriodText : currentTimeText;

		builder.append( dateRange ).append( "<br />" );

		if ( dateRangeType != DateRangeType.CURRENT_TIME ) {
			builder.append( translate( "from" ) ).append( ": " ).append( services.getDateUtilsService().formatDate( jobDateRange.getStartDate() ) ).append( "<br />" );
			builder.append( translate( "to" ) ).append( ": " ).append( services.getDateUtilsService().formatDate( jobDateRange.getEndDate() ) ).append( "<br />" );
		}
	}

	protected void setDateRangeParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {

		final int dateRangeTypeId = jobParameters.get( SavedJobParameterKey.PARAM_DATE_RANGE_TYPE_ID ).getValueInt();
		final DateRangeType dateRangeType = DateRangeType.getById( dateRangeTypeId );

		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final Date dateFrom = jobParameters.get( SavedJobParameterKey.PARAM_DATE_FROM ).getValueTime( dateUtilsService );
		final Date dateTo = jobParameters.get( SavedJobParameterKey.PARAM_DATE_TO ).getValueTime( dateUtilsService );

		final int timePeriod = jobParameters.get( SavedJobParameterKey.PARAM_TIME_PERIOD ).getValueInt();

		jobDateRange = new JobDateRange( dateRangeType, dateFrom, dateTo, timePeriod, dateUtilsService );
	}

	protected void getDateRangeParametersMap( final Map<SavedJobParameterKey, CommonProperty> parametersMap ) {

		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		parametersMap.put( SavedJobParameterKey.PARAM_DATE_RANGE_TYPE_ID, new CommonProperty( SavedJobParameterKey.PARAM_DATE_RANGE_TYPE_ID.getId(), jobDateRange.getDateRangeType().getId() ) );

		parametersMap.put( SavedJobParameterKey.PARAM_DATE_FROM, new CommonProperty( SavedJobParameterKey.PARAM_DATE_FROM.getId(), jobDateRange.getDateFrom(), dateUtilsService ) );
		parametersMap.put( SavedJobParameterKey.PARAM_DATE_TO, new CommonProperty( SavedJobParameterKey.PARAM_DATE_TO.getId(), jobDateRange.getDateTo(), dateUtilsService ) );

		parametersMap.put( SavedJobParameterKey.PARAM_TIME_PERIOD, new CommonProperty( SavedJobParameterKey.PARAM_TIME_PERIOD.getId(), jobDateRange.getTimePeriod() ) );
	}

	public JobDateRange getJobDateRange() {
		return jobDateRange;
	}

	public void setJobDateRange( final JobDateRange jobDateRangeTemp ) {
		this.jobDateRange = jobDateRangeTemp;
	}
}
