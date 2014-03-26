package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryService;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import core.services.translator.TranslatorService;
import utils.ListUtils;
import utils.NumberUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class JobExecutionHistoryCleanupJob extends AbstractJob {

	private static final int ITEMS_IN_BUNCH_TO_DELETE = 50;

	private int deleteEntriesOlderThenDays;
	private List<JobExecutionStatus> jobExecutionStatusesToDelete;

	public JobExecutionHistoryCleanupJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( JobExecutionHistoryCleanupJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

		final JobExecutionHistoryService jobExecutionHistoryService = services.getJobExecutionHistoryService();
		final List<Integer> idsToBeDeleted = getTotalItemsToDelete( jobExecutionHistoryService );

		increment();

		final int totalItemsToDelete = idsToBeDeleted.size();
		final int total = getTotalSteps( totalItemsToDelete );

		for ( int i = 0; i < total; i++ ) {
			final int from = i * ITEMS_IN_BUNCH_TO_DELETE;
			int to = ( i + 1 ) * ITEMS_IN_BUNCH_TO_DELETE - 1;

			if ( to >= totalItemsToDelete ) {
				to = totalItemsToDelete;
			}

			final List<Integer> deleteBunch = idsToBeDeleted.subList( from, to );
			log.debug( String.format( "From: %d, to: %d", from, to ) );

			setProgress( to );

			jobExecutionHistoryService.delete( deleteBunch );

			if ( isFinished() ) {
				break;
			}

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}
		}

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

		final List<Integer> idsToBeDeleted = getTotalItemsToDelete( services.getJobExecutionHistoryService() );
		totalJopOperations = idsToBeDeleted.size(); //getTotalSteps( idsToBeDeleted.size() );
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
				final String execStatusTranslated = translatorService.translate( jobExecutionStatus.getName() );
				final String img = String.format( "<img src='%s/jobExecutionStatus/%s' height='16' title='%s'> "
					, services.getUrlUtilsService().getSiteImagesPath(), jobExecutionStatus.getIcon(), execStatusTranslated );
				builder.append( img ).append( execStatusTranslated ).append( "</li>" );
				builder.append( "<br />" );
			}
		} else {
			builder.append( translatorService.translate( "All" ) );
		}

		/*builder.append( "<br />" );
		builder.append( translatorService.translate( "Total" ) ).append( ": " );
		builder.append( getTotalItemsToDelete( services.getJobExecutionHistoryService() ).size() );*/

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

	private List<Integer> getTotalItemsToDelete( final JobExecutionHistoryService jobExecutionHistoryService ) {
		final Date timeFrame = services.getDateUtilsService().getFirstSecondOfTheDayNDaysAgo( deleteEntriesOlderThenDays );
		return jobExecutionHistoryService.getEntriesIdsOlderThen( timeFrame, jobExecutionStatusesToDelete );
	}

	private int getTotalSteps( final int totalItemsToDelete ) {
		return ( int ) NumberUtils.ceil( ( float ) totalItemsToDelete / ITEMS_IN_BUNCH_TO_DELETE );
	}
}
