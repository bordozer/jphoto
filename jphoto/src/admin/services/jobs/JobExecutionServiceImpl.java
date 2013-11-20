package admin.services.jobs;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.GenerationMonitor;
import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.general.JobProgressDTO;
import admin.jobs.entries.JobChainJob;
import admin.jobs.general.JobStatusChangeStrategy;
import admin.jobs.general.SavedJob;
import admin.jobs.enums.JobRunMode;
import core.log.LogHelper;
import core.services.security.Services;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import core.services.utils.DateUtilsService;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class JobExecutionServiceImpl implements JobExecutionService {

	@Autowired
	private Services services;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private JobExecutionHistoryService jobExecutionHistoryService;

	@Autowired
	private SavedJobService savedJobService;

	@Autowired
	private JobStatusChangeStrategyService jobStatusChangeStrategyService;

	private final List<AbstractJob> activeJobs = newArrayList();

	private LogHelper log = new LogHelper( JobExecutionServiceImpl.class );

	@Override
	public void execute( final AbstractJob job ) {

		final GenerationMonitor generationMonitor = job.getGenerationMonitor();
		generationMonitor.setStatus( JobExecutionStatus.WAITING_FOR_START );

		initJobServices( job );

		job.setJobId( createJobExecutionHistoryEntry( job ) );

		if ( !activeJobs.contains( job ) ) {
			activeJobs.add( job );
		}

		job.setStartTime( dateUtilsService.getCurrentTime() );

		generationMonitor.resetCounter();
		generationMonitor.setTotal( job.getTotalJopOperations() );

		if ( job instanceof JobChainJob ) {
			runChainJobs( job );
		}

		job.start();
	}

	@Override
	public void changeJobStatus( final JobStatusChangeStrategy statusChangeStrategy ) {
		statusChangeStrategy.changeStatus();
	}

	@Override
	public void changeJobStatus_Done( final AbstractJob job ) {
		changeJobStatus( jobStatusChangeStrategyService.doneSuccessfully( job ) );
	}

	@Override
	public void changeJobStatus_InProgress( final AbstractJob job ) {
		changeJobStatus( jobStatusChangeStrategyService.inProgress( job ) );
	}

	@Override
	public void breakJobExecutionChain( final AbstractJob failedJob, final JobStatusChangeStrategy statusChangeStrategy ) {
		breakJobExecutionChain( failedJob, statusChangeStrategy, false );
	}

	@Override
	public void breakJobExecutionChain( final AbstractJob failedJob, final JobStatusChangeStrategy statusChangeStrategy, final boolean forceStopChild ) {
		final AbstractJob jobToStop = statusChangeStrategy.getJob();

		changeJobStatus( statusChangeStrategy );

		breakParentJobChainIfNecessary( jobToStop );

		if ( ! jobToStop.isChainJob() ) {
			return;
		}

		final JobChainJob chainJobToStop = ( JobChainJob ) jobToStop;

		boolean stopJobChildren = false;
		if ( ! forceStopChild ) {
			stopJobChildren = chainJobToStop.getJobRunMode() == JobRunMode.SERIALLY && chainJobToStop.isBreakChainExecutionIfError();
			if ( ! stopJobChildren ) {
				log.info( String.format( "" ) );
				chainJobToStop.notifyAllChildrenJobsForParallelExecution();
				return;
			}
		}

		// break children
		final Map<Integer, AbstractJob> childJobsToStop = chainJobToStop.getDependantJobsMap();
		for ( final int savedJobId : childJobsToStop.keySet() ) {

			final AbstractJob childJobToStop = childJobsToStop.get( savedJobId );

			if ( childJobToStop.isActive() ) {
				breakJobExecutionChain( failedJob, jobStatusChangeStrategyService.cancelledByErrorInParent( childJobToStop, failedJob ), forceStopChild || stopJobChildren );
			}
		}
	}

	@Override
	public void stopJobWithChildByUserDemand( final int jobId ) {
		stopJobWithChild( getActiveJob( jobId ), null );
	}

	@Override
	public void removeJobFromActiveList( final AbstractJob job ) {
		activeJobs.remove( job );
	}

	@Override
	public GenerationMonitor getMonitor( final AbstractJob job ) {
		return job.getGenerationMonitor();
	}

	@Override
	public JobProgressDTO getJobProgressAjax( final int jobId ) {
		final JobProgressDTO result = new JobProgressDTO();

		final JobExecutionHistoryEntry historyEntry = jobExecutionHistoryService.load( jobId );
		if ( historyEntry == null ) {
			result.setJobStatusId( JobExecutionStatus.ERROR.getId() );
			log.error( String.format( "Job with JobId='%d' not found", jobId ) );
			return result;
		}

		result.setCurrent( historyEntry.getCurrentJobStep() );
		result.setTotal( historyEntry.getTotalJobSteps() );
		result.setJobStatusId( historyEntry.getJobExecutionStatus().getId() );
		result.setJobActive( historyEntry.getJobExecutionStatus().isActive() );
		result.setJobExecutionDuration( dateUtilsService.formatTime( historyEntry.getExecutionDuration() ) );

		return result;
	}

	@Override
	public List<AbstractJob> getActiveJobs() {
		return activeJobs;
	}

	@Override
	public AbstractJob getActiveJob( final int jobId ) {
		for ( final AbstractJob activeJob : activeJobs ) {
			if ( activeJob.getJobId() == jobId ) {
				return activeJob;
			}
		}
		return null;
	}

	@Override
	public void initJobServices( final AbstractJob job ) {
		job.setServices( services );
	}


	private void breakParentJobChainIfNecessary( final AbstractJob failedJob ) {

		if ( ! failedJob.hasParentJob() ) {
			return; // single job and there is nothing to break
		}

		final JobChainJob parentJob = ( JobChainJob ) failedJob.getParentJob(); // parent job could be only JobChainJob

		breakJobExecutionChain( failedJob, jobStatusChangeStrategyService.cancelledByErrorInChild( parentJob, failedJob ) );
	}

	private void runChainJobs( final AbstractJob job ) {

		final JobChainJob chainJob = ( JobChainJob ) job;

		final List<SavedJob> savedJobs = newArrayList();
		for ( final int savedJobId : chainJob.getSavedJobToExecuteIds() ) {
			final SavedJob savedJob = savedJobService.load( savedJobId );
			if ( savedJob.isActive() ) {
				savedJobs.add( savedJob );
			}
		}

		for ( final SavedJob savedJob : savedJobs ) {
			final AbstractJob jobToExecute = savedJob.getJob();

			chainJob.addDependantJob( jobToExecute );

			execute( jobToExecute );
		}
	}

	private int createJobExecutionHistoryEntry( final AbstractJob job ) {
		final JobExecutionHistoryEntry jobExecutionHistoryEntry = new JobExecutionHistoryEntry();

		jobExecutionHistoryEntry.setSavedJobType( job.getJobType() );
		jobExecutionHistoryEntry.setStartTime( dateUtilsService.getCurrentTime() );
		jobExecutionHistoryEntry.setJobExecutionStatus( JobExecutionStatus.WAITING_FOR_START );
		jobExecutionHistoryEntry.setParametersDescription( job.getJobParametersDescription() );

		final int savedJobId = job.getSavedJobId();
		if ( savedJobId > 0 ) {
			jobExecutionHistoryEntry.setSavedJob( savedJobService.load( savedJobId ) );
		}

		jobExecutionHistoryEntry.setParametersMap( job.getParametersMap() );
		jobExecutionHistoryEntry.setCurrentJobStep( 0 );
		jobExecutionHistoryEntry.setTotalJobSteps( job.getTotalJopOperations() );
		jobExecutionHistoryEntry.setScheduledTaskId( job.getScheduledTaskId() );

		jobExecutionHistoryService.save( jobExecutionHistoryEntry );

		return jobExecutionHistoryEntry.getId();
	}

	private void stopJobWithChild( final AbstractJob job, final AbstractJob parentJob ) {

		if ( job == null ) {
			return;
		}

		final Map<Integer, AbstractJob> dependantJobs = job.getDependantJobsMap();
		for ( final int savedJobId : dependantJobs.keySet() ) {
			stopJobWithChild( dependantJobs.get( savedJobId ), job );
		}

		if ( dateUtilsService.isEmptyTime( job.getFinishTime() ) ) {
			job.setFinishTime( dateUtilsService.getCurrentTime() );
		}

		if ( ! job.isActive() ) {
			return;
		}


		if ( parentJob == null ) {
			jobStatusChangeStrategyService.stoppedByUser( job ).changeStatus();
			return;
		}

		final JobExecutionStatus executionStatus = job.getGenerationMonitor().getStatus();
		if ( executionStatus == JobExecutionStatus.IN_PROGRESS ) {
			jobStatusChangeStrategyService.stoppedByParentJob( job, parentJob ).changeStatus();
			return;
		}

		jobStatusChangeStrategyService.cancelledByParentJob( job, parentJob ).changeStatus();
	}
}
