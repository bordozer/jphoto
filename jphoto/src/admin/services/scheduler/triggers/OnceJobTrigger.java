package admin.services.scheduler.triggers;

import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.OnceExecutionTask;
import core.services.utils.DateUtilsService;
import org.quartz.Trigger;

import static org.quartz.TriggerBuilder.newTrigger;

public class OnceJobTrigger extends AbstractJobTrigger {

	public OnceJobTrigger( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {
		super( schedulerTaskId, executionTask, dateUtilsService );
	}

	@Override
	public Trigger createTrigger() {

		final OnceExecutionTask onceExecutionTask = ( OnceExecutionTask ) executionTask;

		return newTrigger()
					.withIdentity( getTriggerIdentity(), getGroupIdentity() )
					.startAt( onceExecutionTask.getStartTaskTime() )
					.build();
	}

	@Override
	public boolean skipSchedulingJob() {
		return ! executionTask.isExecutionTaskActive()
			   || ( executionTask.isSkipMissedExecutions() && trigger.getFinalFireTime().getTime() < dateUtilsService.getCurrentTime().getTime() );
	}
}
