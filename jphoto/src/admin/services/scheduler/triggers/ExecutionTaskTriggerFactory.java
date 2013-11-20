package admin.services.scheduler.triggers;

import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.ExecutionTaskType;
import core.services.utils.DateUtilsService;

public class ExecutionTaskTriggerFactory {

	private final AbstractJobTrigger jobTrigger;

	public ExecutionTaskTriggerFactory( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {
		this.jobTrigger = getTriggerInstance( schedulerTaskId, executionTask, dateUtilsService );
	}

	private AbstractJobTrigger getTriggerInstance( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {

		final ExecutionTaskType taskType = executionTask.getTaskType();

		switch ( taskType ) {
			case ONCE:
				return new OnceJobTrigger( schedulerTaskId, executionTask, dateUtilsService );
			case PERIODICAL:
				return new PeriodicalJobTrigger( schedulerTaskId, executionTask, dateUtilsService );
			case DAILY:
				return new DailyJobTrigger( schedulerTaskId, executionTask, dateUtilsService );
			case MONTHLY:
				return new MonthlyJobTrigger( schedulerTaskId, executionTask, dateUtilsService );
		}

		throw new IllegalArgumentException( String.format( "Unsupported ExecutionTaskType: %s", taskType ) );
	}

	public static String getTriggerIdentity( final int schedulerTaskId ) {
		return String.format( "quartz_trigger_%d", schedulerTaskId );
	}

	public static String getGroupIdentity( final ExecutionTaskType taskType ) {
		return String.format( "quartz_group_%s", taskType );
	}

	public static String getJobIdentity( final int schedulerTaskId ) {
		return String.format( "quartz_job_%d", schedulerTaskId );
	}

	public AbstractJobTrigger getJobTrigger() {
		return jobTrigger;
	}
}
