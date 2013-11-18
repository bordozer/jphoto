package core.services.job;

import core.general.scheduler.SchedulerTask;
import core.interfaces.AllEntriesLoadable;
import core.interfaces.BaseEntityService;
import org.quartz.SchedulerException;

import java.util.List;

public interface SchedulerService extends BaseEntityService<SchedulerTask>, AllEntriesLoadable<SchedulerTask> {

	int loadIdByName( final String schedulerTaskName );

	boolean isJobScheduled( final int savedJobId );

	void deactivateSchedulerTasks( final List<Integer> ids ) throws SchedulerException;

	void activateSchedulerTasks( final List<Integer> ids ) throws SchedulerException;

	void activateSchedulerTask( final int schedulerTaskId ) throws SchedulerException;

	void deactivateSchedulerTask( final int schedulerTaskId ) throws SchedulerException;
}
