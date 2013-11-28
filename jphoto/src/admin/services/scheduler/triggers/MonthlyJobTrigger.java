package admin.services.scheduler.triggers;

import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.Month;
import core.general.executiontasks.MonthlyExecutionTask;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.quartz.TriggerBuilder.newTrigger;

public class MonthlyJobTrigger extends AbstractJobTrigger {

	protected MonthlyJobTrigger( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {
		super( schedulerTaskId, executionTask, dateUtilsService );
	}

	@Override
	public Trigger createTrigger() {

		final MonthlyExecutionTask monthlyTask = ( MonthlyExecutionTask ) executionTask;

		return newTrigger()
			.withIdentity( getTriggerIdentity(), getGroupIdentity() )
			.startAt( dateUtilsService.extractDate( monthlyTask.getStartTaskDate() ) )
			.endAt( getEndDate( monthlyTask.getEndTaskTime() ) )
			.withSchedule( getCronSchedule() )
			.build();
	}

	@Override
	public boolean skipSchedulingJob() {
		return executionTask.isSuspended();
	}

	private CronScheduleBuilder getCronSchedule() {
		final MonthlyExecutionTask monthlyTask = ( MonthlyExecutionTask ) executionTask;

		final LogHelper log = new LogHelper( MonthlyJobTrigger.class );

		final String[] hh_mm_ss = getCronTime();
		final String cronExpression = String.format( "%s %s %s %s %s ?", hh_mm_ss[2], hh_mm_ss[1], hh_mm_ss[0], getCronDay( monthlyTask ), getCronMonths( monthlyTask ) );

		log.debug( String.format( "Cron Expression: '%s'", cronExpression ) );

		return getCroneBuilder( cronExpression );
	}

	private String getCronDay( final MonthlyExecutionTask monthlyTask ) {
		final int day = monthlyTask.getDayOfMonth();
		return day > -1 ? String.valueOf( day ) : "L";
	}

	private String getCronMonths( final MonthlyExecutionTask monthlyTask ) {
		final List<Month> months = monthlyTask.getMonths();
		final List<String> monthIds = newArrayList();
		for ( final Month month : months ) {
			monthIds.add( month.getCroneSchedulerName() );
		}

		return StringUtils.join( monthIds, "," );
	}
}
