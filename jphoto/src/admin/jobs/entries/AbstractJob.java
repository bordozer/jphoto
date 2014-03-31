package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.GenerationMonitor;
import admin.jobs.general.JobRuntimeLog;
import admin.services.jobs.JobExecutionService;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.user.User;
import core.log.LogHelper;
import core.services.security.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.sql.BaseSqlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.SqlIdsSelectQuery;
import utils.ErrorUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public abstract class AbstractJob extends Thread {

	public static final int OPERATION_COUNT_UNKNOWN = -1;
	protected final GenerationMonitor generationMonitor = new GenerationMonitor();

	protected JobRuntimeEnvironment jobEnvironment;

	protected int jobId;
	protected int savedJobId;
	protected Date startTime;
	protected Date finishTime;

	protected AbstractJob parentJob;
	protected final Map<Integer, AbstractJob> dependantJobsMap = newLinkedHashMap();

	@Autowired
	protected Services services;

	protected final LogHelper log;

	protected int photoQtyLimit;
	protected List<User> beingProcessedUsers;
	protected List<Integer> beingProcessedPhotosIds;

	protected int totalJopOperations;
	private final List<JobRuntimeLog> jobRuntimeLogs = newArrayList();

	private int scheduledTaskId;

	protected abstract void runJob() throws Throwable;

	public abstract Map<SavedJobParameterKey, CommonProperty> getParametersMap();

	public abstract void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters );

	public abstract String getJobParametersDescription();

	public abstract SavedJobType getJobType();

	public AbstractJob( final LogHelper log, final JobRuntimeEnvironment jobEnvironment ) {
		this.log = log;
		this.jobEnvironment = jobEnvironment;
	}

	@Override
	public void run() {

//		log.debug( String.format( "Job #%d thread is running", jobId ) );

		if ( parentJob != null ) {
			synchronized ( this ) {
//				log.debug( String.format( "Job #%d is waiting for notifying from parent chainJob #%d than it may start", jobId, parentJob.getJobId() ) );
				try {
					wait();

//					log.debug( String.format( "Job #%d is starting", jobId ) );
				} catch ( InterruptedException e ) {
					log.error( e );
				}
			}
		}

		initAndRunJob();
	}

	private void initAndRunJob() {

		final JobExecutionService jobExecutionService = services.getJobExecutionService();

		try {

			if ( isParentChainJobWasBrokenByError() ) {
				final JobChainJob parentChainJob = ( JobChainJob ) parentJob;
				jobExecutionService.breakJobExecutionChain( parentChainJob, services.getJobStatusChangeStrategyService().cancelledByErrorInParent( this, parentChainJob ) );
				return;
			}

//			log.info( String.format( "Init job '%s'", this ) );

			beingProcessedUsers = services.getUserService().loadAll();

			beingProcessedPhotosIds = loadPhotoIds( photoQtyLimit );

			finishTime = null;

			log.debug( String.format( "Running job '%s'", this ) );

			// DO NOT EXTRACT VARIABLE generationMonitor.getStatus()!!!
			if ( generationMonitor.getStatus() == JobExecutionStatus.WAITING_FOR_START ) {
				jobExecutionService.changeJobStatus_InProgress( this );
			}

			runJob();

			finishTime = services.getDateUtilsService().getCurrentTime();

			if ( generationMonitor.getStatus() == JobExecutionStatus.IN_PROGRESS ) {
				jobExecutionService.changeJobStatus_Done( this );
			}

			if ( generationMonitor.getStatus().isStoppedOrErrorOrCancelled() ) {
				getLog().error( String.format( "Job '%s'  finished WITH errors", this ) );
			} else {
				getLog().debug( String.format( "Job '%s' finished without errors", this ) );
			}

		} catch ( Throwable e ) {

			final String exceptionMessage = ErrorUtils.getErrorStack( e ).toString();

			jobExecutionService.breakJobExecutionChain( this, services.getJobStatusChangeStrategyService().error( this, exceptionMessage ) );

			log.error( exceptionMessage );

			final String message = String.format( "Job '%s' failed with exception: %s", this, exceptionMessage );
			services.getPrivateMessageService().sendNotificationAboutErrorToAdmins( message );
		} finally {
			jobExecutionService.removeJobFromActiveList( this );
//			log.debug( String.format( "removeJobFromActiveList: %s", this ) );

			if ( parentJob != null ) {
				synchronized ( parentJob ) {
					log.debug( String.format( "'%s' notifies parent job '%s' about finishing", this, parentJob ) );
					parentJob.notify();
				}
			}
		}
	}

	public boolean hasParentJob() {
		return parentJob != null;
	}

	public boolean isChainJob() {
		return this instanceof JobChainJob;
	}

	public boolean hasJobFinishedWithAnyResult() {
		return generationMonitor.getStatus().isNotActive();
	}

	public boolean isFinished() {
		return generationMonitor.getCurrent() >= totalJopOperations;
	}

	public boolean isActive() {
		return generationMonitor.getStatus().isActive();
	}

	public void increment() {
		generationMonitor.increment();
		updateCurrentJobExecutionEntryStep();
	}

	public void increment( final int steps) {
		generationMonitor.increment( steps );
		updateCurrentJobExecutionEntryStep();
	}

	public void setProgress( final int steps) {
		generationMonitor.setCurrent( steps );
		updateCurrentJobExecutionEntryStep();
	}

	public String getJobName() {
		return getJobType().getName();
	}

	public GenerationMonitor getGenerationMonitor() {
		return generationMonitor;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId( final int jobId ) {
		this.jobId = jobId;
	}

	public int getSavedJobId() {
		return savedJobId;
	}

	public void setSavedJobId( final int savedJobId ) {
		this.savedJobId = savedJobId;
	}

	public void setStartTime( final Date startTime ) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime( final Date finishTime ) {
		this.finishTime = finishTime;
	}

	protected LogHelper getLog() {
		return log;
	}

	public void setPhotoQtyLimit( final int photoQtyLimit ) {
		this.photoQtyLimit = photoQtyLimit;
	}

	public int getTotalJopOperations() {
		return totalJopOperations;
	}

	public void setTotalJopOperations( final int totalJopOperations ) {
		this.totalJopOperations = totalJopOperations;
	}

	public AbstractJob getParentJob() {
		return parentJob;
	}

	public void setParentJob( final AbstractJob parentJob ) {
		this.parentJob = parentJob;
	}

	public Map<Integer, AbstractJob> getDependantJobsMap() {
		return dependantJobsMap;
	}

	public void addDependantJob( final AbstractJob job ) {
		synchronized ( dependantJobsMap ) {
			dependantJobsMap.put( job.getSavedJobId(), job );
		}
		job.setParentJob( this );
	}

	public void setServices( final Services services ) {
		this.services = services;
	}

	public List<User> getBeingProcessedUsers() {
		return beingProcessedUsers;
	}

	public List<Integer> getBeingProcessedPhotosIds() {
		return beingProcessedPhotosIds;
	}

	@Override
	public int hashCode() {
		return getJobType().getId() * 31 + jobId;
	}

	@Override
	public String toString() {
		return String.format( "Job %s %s ( #%d )", getJobType().getName(), savedJobId > 0 ? String.format( ", savedJobId = %d", savedJobId ): "", jobId );
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( ! ( obj instanceof AbstractJob ) ) {
			return false;
		}

		final AbstractJob job = ( AbstractJob ) obj;
		return job.getJobId() == jobId;
	}

	protected List<Integer> loadPhotoIds( final int lastPhotoQtyLimit ) {
		final BaseSqlUtilsService baseSqlUtilsService = services.getBaseSqlUtilsService();

		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );

		if ( lastPhotoQtyLimit > 0 ) {
			selectQuery.setLimit( lastPhotoQtyLimit );
		}

		return services.getPhotoService().load( selectQuery ).getIds();
	}

	private void updateCurrentJobExecutionEntryStep() {
		services.getJobExecutionHistoryService().updateCurrentJobStep( jobId, generationMonitor.getCurrent() );
	}

	private boolean isParentChainJobWasBrokenByError() {

		if ( parentJob == null) {
			return false;
		}

		if ( parentJob.isChainJob() ) {
			final JobChainJob parentChainJob = ( JobChainJob ) parentJob;
			return parentJob.getGenerationMonitor().getStatus().isStoppedOrErrorOrCancelled() && parentChainJob.isBreakChainExecutionIfError();
		}

		return false;
	}

	public List<JobRuntimeLog> getJobRuntimeLogs() {
		return jobRuntimeLogs;
	}

	public void addJobRuntimeLogMessage( final TranslatableMessage translatableMessage ) {
//		synchronized ( jobRuntimeLogs ) {
			jobRuntimeLogs.add( new JobRuntimeLog( translatableMessage, services.getDateUtilsService().getCurrentTime() ) );
//		}
	}

	public int getScheduledTaskId() {
		return scheduledTaskId;
	}

	public void setScheduledTaskId( final int scheduledTaskId ) {
		this.scheduledTaskId = scheduledTaskId;
	}

	protected Language getSystemDefaultLanguage() {
		return services.getSystemVarsService().getSystemDefaultLanguage();
	}

	protected String translate( final String actions ) {
		return services.getTranslatorService().translate( actions, jobEnvironment.getLanguage() );
	}

	public JobRuntimeEnvironment getJobEnvironment() {
		return jobEnvironment;
	}

	public Language getLanguage() {
		return jobEnvironment.getLanguage();
	}
}
