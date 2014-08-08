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

		final String dateRangeText = dateRangeType.getName();
		final String timePeriodText = translatorService.translate( "Job DateRangeParameters: $1 $2 days", getLanguage(), dateRangeText, String.valueOf( jobDateRange.getTimePeriod() ) );
		final String name = translatorService.translate( DateRangeType.CURRENT_TIME.getName(), getLanguage() );
		final String currentTimeText = translatorService.translate( "Job DateRangeParameters: Time: $1", getLanguage(), name );

		final String dateRange = dateRangeType == DateRangeType.DATE_RANGE ? dateRangeText : dateRangeType == DateRangeType.TIME_PERIOD ? timePeriodText : currentTimeText;

		builder.append( dateRange ).append( "<br />" );

		if ( dateRangeType != DateRangeType.CURRENT_TIME ) {
			builder.append( translate( "Job DateRangeParameters: from" ) ).append( ": " ).append( services.getDateUtilsService().formatDate( jobDateRange.getStartDate() ) ).append( "<br />" );
			builder.append( translate( "Job DateRangeParameters: to" ) ).append( ": " ).append( services.getDateUtilsService().formatDate( jobDateRange.getEndDate() ) ).append( "<br />" );
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
