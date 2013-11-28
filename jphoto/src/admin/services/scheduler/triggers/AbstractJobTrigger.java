package admin.services.scheduler.triggers;

import core.general.executiontasks.*;
import core.services.utils.DateUtilsService;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

import java.util.Date;

public abstract class AbstractJobTrigger {

	protected final int schedulerTaskId;
	protected final AbstractExecutionTask executionTask;

	protected final Trigger trigger;

	protected final DateUtilsService dateUtilsService;

	public abstract Trigger createTrigger();

	protected AbstractJobTrigger( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {
		this.schedulerTaskId = schedulerTaskId;
		this.executionTask = executionTask;
		this.dateUtilsService = dateUtilsService;

		this.trigger = createTrigger();
	}

	public boolean skipSchedulingJob() {
		return ! executionTask.isExecutionTaskActive();
	}

	public final Trigger getTrigger() {
		return trigger;
	}

	protected final String getTriggerIdentity() {
		return ExecutionTaskTriggerFactory.getTriggerIdentity( this.schedulerTaskId );
	}

	protected final String getGroupIdentity() {
		return ExecutionTaskTriggerFactory.getGroupIdentity( this.executionTask.getTaskType() );
	}

	protected Date getEndDate( final Date endTaskTime ) {
		if ( endTaskTime != null ) {
			return dateUtilsService.extractDate( endTaskTime );
		}
		return null;
	}

	protected String[] getCronTime() {
		final Date startTaskTime = executionTask.getStartTaskTime();
		final String executionTime = dateUtilsService.formatTime( startTaskTime );

		final String[] hh_mm_ss = executionTime.split( ":" );

		for ( int i = 0 ; i < hh_mm_ss.length; i++ ) {
			if ( hh_mm_ss[i].equals( "00" ) ) {
				hh_mm_ss[i] = "0";
			}
		}

		return hh_mm_ss;
	}

	protected CronScheduleBuilder getCroneBuilder( final String cronExpression ) {
		return CronScheduleBuilder.cronSchedule( cronExpression ).withMisfireHandlingInstructionDoNothing();
	}
}
