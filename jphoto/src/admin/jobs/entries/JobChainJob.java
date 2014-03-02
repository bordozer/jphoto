package admin.jobs.entries;

import admin.jobs.enums.JobRunMode;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class JobChainJob extends AbstractJob {

	private List<Integer> savedJobToExecuteIds;
	private JobRunMode jobRunMode;
	private boolean breakChainExecutionIfError;

	public JobChainJob() {
		super( new LogHelper( JobChainJob.class ) );
	}

	@Override
	protected void runJob() throws Throwable {

		if ( jobRunMode == JobRunMode.PARALLEL ) {
			notifyAllChildrenJobsForParallelExecution();

			while( true ) {
				synchronized ( this ) {
					log.debug( String.format( "'%s' is waiting for child jobs execution", this ) );
					wait();

					log.debug( String.format( "'%s': one of the child job executed", this ) );

					if ( hasJobFinishedWithAnyResult() ) {
						break;
					}

					increment();

					if ( isFinished() ) {
						log.debug( String.format( "%s: all child jobs are finished", this ) );
						break;
					}
				}
			}
		} else {
			// run child job one by one
			for ( final int savedJobId : dependantJobsMap.keySet() ) {

				synchronized ( dependantJobsMap.get( savedJobId ) ) {
					log.debug( String.format( "'%s' runs child job serially: %s", this, dependantJobsMap.get( savedJobId ) ) );
					dependantJobsMap.get( savedJobId ).notify();
				}

				synchronized ( this ) {
					log.debug( String.format( "%s: is waiting for child job '%s' finishing", this, dependantJobsMap.get( savedJobId ) ) );
					wait();
				}

				log.debug( String.format( "%s: child job '%s' is finished", this, dependantJobsMap.get( savedJobId ) ) );

				if ( hasJobFinishedWithAnyResult() ) {
					break;
				}

				increment();

				if ( isFinished() ) {
					log.debug( String.format( "%s: all child job are finished", this ) );
					break;
				}
			}
		}
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		final SavedJobParameterKey key = SavedJobParameterKey.PARAM_SAVED_JOB_CHAIN;
		parametersMap.put( key, CommonProperty.createFromIntegerList( key.getId(), savedJobToExecuteIds, dateUtilsService ) );

		parametersMap.put( SavedJobParameterKey.JOB_RUN_MODE_ID, new CommonProperty( SavedJobParameterKey.JOB_RUN_MODE_ID.getId(), jobRunMode.getId() ) );
		parametersMap.put( SavedJobParameterKey.BREAK_CHAIN_EXECUTION_IF_ERROR, new CommonProperty( SavedJobParameterKey.BREAK_CHAIN_EXECUTION_IF_ERROR.getId(), breakChainExecutionIfError ) );

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		savedJobToExecuteIds = jobParameters.get( SavedJobParameterKey.PARAM_SAVED_JOB_CHAIN ).getValueListInt();
		jobRunMode = JobRunMode.getById( jobParameters.get( SavedJobParameterKey.JOB_RUN_MODE_ID ).getValueInt() );
		breakChainExecutionIfError = jobParameters.get( SavedJobParameterKey.BREAK_CHAIN_EXECUTION_IF_ERROR ).getValueBoolean();

		totalJopOperations = savedJobToExecuteIds.size();
	}

	@Override
	public String getJobParametersDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Run mode:" ) ).append( jobRunMode.getNameTranslated() ).append( "<br />" );
		builder.append( translatorService.translate( "Stop dependant jobs if error:" ) ).append( translatorService.translate( breakChainExecutionIfError ? "Yes" : "No" ) ).append( "<br />" );
		builder.append( translatorService.translate( "Jobs:" ) ).append( "<br />" );
		for ( final int savedJobToExecuteId : savedJobToExecuteIds ) {
			final SavedJob savedJob = services.getSavedJobService().load( savedJobToExecuteId );
			final String img = String.format( "<img width='16' height='16' src='%s/jobtype/%s' title='%s'>"
				, services.getUrlUtilsService().getSiteImagesPath(), savedJob.getJobType().getIcon(), savedJob.getJobType().getNameTranslated() );
			builder.append( "<br />" ).append( img ).append( " " ).append( services.getEntityLinkUtilsService().getAdminSavedJobLink( savedJob.getJobType(), savedJob ) );
		}

		return builder.toString();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.JOB_CHAIN;
	}

	public List<Integer> getSavedJobToExecuteIds() {
		return savedJobToExecuteIds;
	}

	public void setSavedJobToExecuteIds( final List<Integer> savedJobToExecuteIds ) {
		this.savedJobToExecuteIds = savedJobToExecuteIds;
	}

	public void setJobRunMode( final JobRunMode jobRunMode ) {
		this.jobRunMode = jobRunMode;
	}

	public JobRunMode getJobRunMode() {
		return jobRunMode;
	}

	public boolean isBreakChainExecutionIfError() {
		return breakChainExecutionIfError;
	}

	public void setBreakChainExecutionIfError( final boolean breakChainExecutionIfError ) {
		this.breakChainExecutionIfError = breakChainExecutionIfError;
	}

	public void notifyAllChildrenJobsForParallelExecution() {
		// run all children jobs
		for ( final int savedJobId : dependantJobsMap.keySet() ) {
			synchronized ( dependantJobsMap.get( savedJobId ) ) {
				log.debug( String.format( "'%s' runs child job: %s", this, dependantJobsMap.get( savedJobId ) ) );
				dependantJobsMap.get( savedJobId ).notify();
			}
		}
	}
}
