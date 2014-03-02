package admin.services.scheduler;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.ExecutionTaskFactory;
import core.general.scheduler.SchedulerTask;
import core.log.LogHelper;
import core.services.dao.SchedulerTasksDao;
import core.services.utils.DateUtilsService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newLinkedHashSet;

public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private DateUtilsService dateUtilsService;

	private final Set<SchedulerTaskProperty> schedulerTaskProperties = newLinkedHashSet();

	{
		Collections.addAll( schedulerTaskProperties, SchedulerTaskProperty.values() );
	}

	@Autowired
	private SchedulerTasksDao schedulerTasksDao;

	@Autowired
	private ScheduledTasksExecutionService scheduledTasksExecutionService;

	private final LogHelper log = new LogHelper( SchedulerServiceImpl.class );

	@Override
	public boolean save( final SchedulerTask entry ) {
		if ( !schedulerTasksDao.saveToDB( entry ) ) {
			return false;
		}

		boolean result = true;

		final AbstractExecutionTask executionTask = entry.getExecutionTask();
		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = executionTask.getParametersMap();

		final int schedulerTaskId = entry.getId();

		schedulerTasksDao.deleteProperties( schedulerTaskId );

		for ( final SchedulerTaskProperty key : parametersMap.keySet() ) {
			result &= schedulerTasksDao.saveProperties( schedulerTaskId, key, parametersMap.get( key ).getValue() );
		}

		try {
			scheduledTasksExecutionService.reScheduleTask( entry );
		} catch ( SchedulerException e ) {
			log.error( String.format( "Can not schedule task: %d", schedulerTaskId ), e );
		}

		return result;
	}

	@Override
	public SchedulerTask load( final int schedulerTaskId ) {
		final SchedulerTask schedulerTask = schedulerTasksDao.load( schedulerTaskId );

		if ( schedulerTask == null ) {
			return null;
		}

		final AbstractExecutionTask executionTask = ExecutionTaskFactory.createInstance( schedulerTask.getTaskType(), getParametersMap( schedulerTaskId ), dateUtilsService );
		schedulerTask.setExecutionTask( executionTask );

		return schedulerTask;
	}

	@Override
	public List<SchedulerTask> loadAll() {
		final List<SchedulerTask> schedulerTasks = schedulerTasksDao.loadAll(); // TODO: load only IDs

		for ( final SchedulerTask schedulerTask : schedulerTasks ) {
			final AbstractExecutionTask executionTask = ExecutionTaskFactory.createInstance( schedulerTask.getTaskType(), getParametersMap( schedulerTask.getId() ), dateUtilsService );
			schedulerTask.setExecutionTask( executionTask );
		}

		return schedulerTasks;
	}

	@Override
	public int loadIdByName( final String schedulerTaskName ) {
		return schedulerTasksDao.loadIdByName( schedulerTaskName );
	}

	@Override
	public boolean delete( final int entryId ) {
		try {
			scheduledTasksExecutionService.unScheduleTask( entryId );
			scheduledTasksExecutionService.deleteJob( entryId );
		} catch ( final SchedulerException e ) {
			log.error( String.format( "Can not remove task from scheduler: %d", entryId ), e );
		}

		schedulerTasksDao.deleteProperties( entryId );
		return schedulerTasksDao.delete( entryId );
	}

	@Override
	public boolean isJobScheduled( final int savedJobId ) {
		return schedulerTasksDao.isJobScheduled( savedJobId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return schedulerTasksDao.exists( entryId );
	}

	@Override
	public boolean exists( final SchedulerTask entry ) {
		return schedulerTasksDao.exists( entry );
	}

	@Override
	public void deactivateSchedulerTasks( final List<Integer> ids ) throws SchedulerException {
		for ( final int taskId : ids ) {
			deactivateSchedulerTask( taskId );
		}
	}

	@Override
	public void deactivateSchedulerTask( final int schedulerTaskId ) throws SchedulerException {

		if ( isScheduledTaskActive( schedulerTaskId ) ) {
			setSchedulerTaskActivity( schedulerTaskId, false );
		}

		if ( scheduledTasksExecutionService.isTaskScheduled( schedulerTaskId ) ) {
			scheduledTasksExecutionService.unScheduleTask( schedulerTaskId );
			scheduledTasksExecutionService.deleteJob( schedulerTaskId );
		}
	}

	@Override
	public void activateSchedulerTasks( final List<Integer> ids ) throws SchedulerException {
		for ( final int taskId : ids ) {
			activateSchedulerTask( taskId );
		}
	}

	@Override
	public void activateSchedulerTask( final int schedulerTaskId ) throws SchedulerException {

		if ( ! isScheduledTaskActive( schedulerTaskId ) ) {
			setSchedulerTaskActivity( schedulerTaskId, true );
		}

		if ( ! scheduledTasksExecutionService.isTaskScheduled( schedulerTaskId ) ) {
			scheduledTasksExecutionService.scheduleTask( schedulerTaskId );
		}
	}

	private void setSchedulerTaskActivity( final int schedulerTaskId, final boolean isActive ) {
		final SchedulerTask schedulerTask = load( schedulerTaskId );
		final AbstractExecutionTask executionTask = schedulerTask.getExecutionTask();
		executionTask.setExecutionTaskActive( isActive );

		save( schedulerTask );
	}

	private boolean isScheduledTaskActive( final int schedulerTaskId ) {
		return load( schedulerTaskId ).getExecutionTask().isExecutionTaskActive();
	}

	private Map<SchedulerTaskProperty, CommonProperty> getParametersMap( final int schedulerTaskId ) {
		final Map<SchedulerTaskProperty, CommonProperty> result = newLinkedHashMap();

		for ( final SchedulerTaskProperty property : schedulerTaskProperties ) {
			final String propertyValue = schedulerTasksDao.getSchedulerTaskPropertyValue( schedulerTaskId, property );
			result.put( property, new CommonProperty( property.getId(), propertyValue ) );
		}

		return result;
	}
}
