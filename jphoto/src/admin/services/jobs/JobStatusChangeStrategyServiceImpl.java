package admin.services.jobs;

import admin.jobs.entries.AbstractJob;
import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.general.JobStatusChangeStrategy;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;

public class JobStatusChangeStrategyServiceImpl implements JobStatusChangeStrategyService {

	@Autowired
	private JobExecutionService jobExecutionService;

	@Autowired
	private JobExecutionHistoryService jobExecutionHistoryService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public JobStatusChangeStrategy doneSuccessfully( final AbstractJob job ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.DONE;
			}

			@Override
			public String getMessage() {
				return translatorService.translate( "Job $1 is performed successfully", getLanguage(), String.valueOf( job.getJobId() ) );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy inProgress( final AbstractJob job ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.IN_PROGRESS;
			}
		};
	}

	@Override
	public JobStatusChangeStrategy error( final AbstractJob job, final String exceptionMessage ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.ERROR;
			}

			@Override
			public String getMessage() {
				return String.format( "Job execution finished with error:<br /><br />%s", exceptionMessage );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy stoppedByUser( final AbstractJob job ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.STOPPED_BY_USER;
			}

			@Override
			public String getMessage() {
				return translatorService.translate( "Job $1 is stopped by user", getLanguage(), String.valueOf( job.getId() ) );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy stoppedByParentJob( final AbstractJob job, final AbstractJob parentJob ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
//				changeStatus( job );
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.STOPPED_BY_PARENT_JOB;
			}

			@Override
			public String getMessage() {
				return translatorService.translate( "Stopped by parent job #$1", getLanguage(), String.valueOf( parentJob.getJobId() ) );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy cancelledByParentJob( final AbstractJob job, final AbstractJob parentJob ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.CANCELLED_BY_PARENT_JOB;
			}

			@Override
			public String getMessage() {
				return translatorService.translate( "Job is cancelled by parent job #$1", getLanguage(), String.valueOf( parentJob.getJobId() ) );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy cancelledByErrorInChild( final AbstractJob job, final AbstractJob failedChildJob ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.STOPPED_BECAUSE_OF_ERROR_IN_CHILD;
			}

			@Override
			public String getMessage() {
				return translatorService.translate( "Job is stopped because of error in child job #$1", getLanguage(), String.valueOf( failedChildJob.getJobId() ) );
			}
		};
	}

	@Override
	public JobStatusChangeStrategy cancelledByErrorInParent( final AbstractJob job, final AbstractJob failedParentJob ) {
		return new JobStatusChangeStrategy( job ) {
			@Override
			public void performCustomActions() {
				jobExecutionHistoryService.setJobExecutionHistoryEntryStatus( job.getJobId(), getMessage(), getJobStatus() );
				removeFromActiveJobsList( job );
			}

			@Override
			public JobExecutionStatus getJobStatus() {
				return JobExecutionStatus.CANCELLED_BECAUSE_OF_ERROR_IN_PARENT;
			}

			@Override
			public String getMessage() {
				return String.format( "Cancelled because parent job #%d finished with error. Job did not start.", failedParentJob.getJobId() );
			}
		};
	}

	private Language getLanguage() {
		return systemVarsService.getSystemDefaultLanguage();
	}

	private void removeFromActiveJobsList( final AbstractJob job ) {
		jobExecutionService.removeJobFromActiveList( job );
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}

	public void setJobExecutionService( final JobExecutionService jobExecutionService ) {
		this.jobExecutionService = jobExecutionService;
	}

	public void setJobExecutionHistoryService( final JobExecutionHistoryService jobExecutionHistoryService ) {
		this.jobExecutionHistoryService = jobExecutionHistoryService;
	}

	public void setTranslatorService( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}
}
