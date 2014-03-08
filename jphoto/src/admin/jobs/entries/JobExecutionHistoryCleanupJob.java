package admin.jobs.entries;

import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryService;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import core.services.translator.TranslatorService;
import utils.ListUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class JobExecutionHistoryCleanupJob extends AbstractJob {

	private int deleteEntriesOlderThenDays;
	private List<JobExecutionStatus> jobExecutionStatusesToDelete;

	public JobExecutionHistoryCleanupJob() {
		super( new LogHelper( JobExecutionHistoryCleanupJob.class ) );
	}

	@Override
	protected void runJob() throws Throwable {
		final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo( deleteEntriesOlderThenDays );

		final JobExecutionHistoryService jobExecutionHistoryService = services.getJobExecutionHistoryService();
		final List<Integer> idsToBeDeleted = jobExecutionHistoryService.getEntriesIdsOlderThen( timeFrame, jobExecutionStatusesToDelete );

		increment();

		jobExecutionHistoryService.delete( idsToBeDeleted );
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.DAYS, new CommonProperty( SavedJobParameterKey.DAYS.getId(), deleteEntriesOlderThenDays ) );

		final List<Integer> statusIds = ListUtils.convertIdentifiableListToListOfIds( jobExecutionStatusesToDelete );
		parametersMap.put( SavedJobParameterKey.ENTRY_TYPES, CommonProperty.createFromIntegerList( SavedJobParameterKey.ENTRY_TYPES.getId(), statusIds, services.getDateUtilsService() ) );

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		deleteEntriesOlderThenDays = jobParameters.get( SavedJobParameterKey.DAYS ).getValueInt();

		final List<Integer> statusIds = jobParameters.get( SavedJobParameterKey.ENTRY_TYPES ).getValueListInt();
		jobExecutionStatusesToDelete = newArrayList();
		for ( final int statusId : statusIds ) {
			jobExecutionStatusesToDelete.add( JobExecutionStatus.getById( statusId ) );
		}

		totalJopOperations = 2;
	}

	@Override
	public String getJobParametersDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Delete entries that finished early then $1 day(s) ago", deleteEntriesOlderThenDays ) ).append( "<br />" );
		builder.append( translatorService.translate( "Job statuses to delete: " ) );

		if ( jobExecutionStatusesToDelete.size() < JobExecutionStatus.values().length ) {
			builder.append( "<br />" );
			for ( final JobExecutionStatus jobExecutionStatus : jobExecutionStatusesToDelete ) {
				final String img = String.format( "<img src='%s/jobExecutionStatus/%s' height='16' title='%s'> "
					, services.getUrlUtilsService().getSiteImagesPath(), jobExecutionStatus.getIcon(), jobExecutionStatus.getNameTranslated() );
				builder.append( img ).append( jobExecutionStatus.getNameTranslated() ).append( "</li>" );
				builder.append( "<br />" );
			}
		} else {
			builder.append( translatorService.translate( "All" ) );
		}

		final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo( deleteEntriesOlderThenDays );
		builder.append( "<br />" );
		builder.append( translatorService.translate( "Total" ) ).append( ": " );
		builder.append( services.getJobExecutionHistoryService().getEntriesIdsOlderThen( timeFrame, jobExecutionStatusesToDelete ).size() );

		return builder.toString();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.JOB_EXECUTION_HISTORY_CLEAN_UP;
	}

	public int getDeleteEntriesOlderThenDays() {
		return deleteEntriesOlderThenDays;
	}

	public void setDeleteEntriesOlderThenDays( final int deleteEntriesOlderThenDays ) {
		this.deleteEntriesOlderThenDays = deleteEntriesOlderThenDays;
	}

	public List<JobExecutionStatus> getJobExecutionStatusesToDelete() {
		return jobExecutionStatusesToDelete;
	}

	public void setJobExecutionStatusesToDelete( final List<JobExecutionStatus> jobExecutionStatusesToDelete ) {
		this.jobExecutionStatusesToDelete = jobExecutionStatusesToDelete;
	}
}
