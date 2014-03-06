package admin.services.scheduler;

import core.general.scheduler.SchedulerTask;
import org.quartz.SchedulerException;

public interface ScheduledTasksExecutionService {

	void start() throws SchedulerException;

	void standby() throws SchedulerException;

//	void pauseAll() throws SchedulerException;

//	void resumeAll() throws SchedulerException;

	boolean isRunning() throws SchedulerException;

	boolean isInStandbyMode() throws SchedulerException;

	boolean isTaskScheduled( final int schedulerTaskId ) throws SchedulerException;

	void scheduleTask( final SchedulerTask schedulerTask ) throws SchedulerException;

	void scheduleTask( final int schedulerTaskId ) throws SchedulerException;

	void unScheduleTask( final int schedulerTaskId ) throws SchedulerException;

	boolean deleteJob( final SchedulerTask schedulerTask ) throws SchedulerException;

	boolean deleteJob( final int schedulerTaskId ) throws SchedulerException;

	void reScheduleTask( final SchedulerTask schedulerTask ) throws SchedulerException;
}
