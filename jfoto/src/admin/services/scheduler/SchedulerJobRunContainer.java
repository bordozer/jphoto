package admin.services.scheduler;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.SavedJob;
import admin.services.jobs.JobExecutionService;
import core.general.scheduler.SchedulerTask;
import core.log.LogHelper;
import admin.services.jobs.SavedJobService;
import org.quartz.*;

public class SchedulerJobRunContainer implements Job {

	public static final String KEY_SCHEDULER_TASK = "schedulerTask";
	public static final String KEY_JOB_EXECUTION_SERVICE = "jobExecutionService";
	public static final String KEY_DATE_UTILS_SERVICE = "dateUtilsService";
	public static final String KEY_SAVED_JOB_SERVICE = "savedJobService";
	public static final String KEY_SAVED_JOB_ID = "savedJobId";

	final LogHelper log = new LogHelper( SchedulerJobRunContainer.class );

	@Override
	public void execute( final JobExecutionContext jobExecutionContext ) throws JobExecutionException {

		final JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

		final SchedulerTask schedulerTask = ( SchedulerTask ) jobDataMap.get( KEY_SCHEDULER_TASK );
		final int schedulerTaskId = schedulerTask.getId();

		final JobExecutionService jobExecutionService = ( JobExecutionService ) jobDataMap.get( KEY_JOB_EXECUTION_SERVICE );
		final SavedJobService savedJobService = ( SavedJobService ) jobDataMap.get( KEY_SAVED_JOB_SERVICE );
		final int savedJobId = ( Integer ) jobDataMap.get( KEY_SAVED_JOB_ID );

		final SavedJob savedJob = savedJobService.load( savedJobId );
		final AbstractJob job = savedJob.getJob(); // what to do

		job.setScheduledTaskId( schedulerTaskId );

		log.info( String.format( "Scheduled job #%s (%s) is about to be executed ( Saved Job '%s')", schedulerTaskId, schedulerTask.getName(), job ) );

		jobExecutionService.execute( job );
	}
}
