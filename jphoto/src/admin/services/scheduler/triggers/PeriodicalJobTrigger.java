package admin.services.scheduler.triggers;

import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.PeriodicalExecutionTask;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;

public class PeriodicalJobTrigger extends AbstractJobTrigger {

	protected PeriodicalJobTrigger( final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService ) {
		super( schedulerTaskId, executionTask, dateUtilsService );
	}

	@Override
	public Trigger createTrigger() {

		final PeriodicalExecutionTask periodicalTask = ( PeriodicalExecutionTask ) executionTask;

		return newTrigger()
			.withIdentity( getTriggerIdentity(), getGroupIdentity() )
			.startAt( periodicalTask.getStartTaskTime() )
			.endAt( periodicalTask.getEndTaskTime() )
			.withSchedule( getCronSchedule() )
			.build();
	}

	private CronScheduleBuilder getCronSchedule() {
		final PeriodicalExecutionTask periodicalTask = ( PeriodicalExecutionTask ) executionTask;

		final LogHelper log = new LogHelper( PeriodicalJobTrigger.class );
		final String[] hh_mm_ss = getCronTime();
		final int period = periodicalTask.getPeriod();

		final String minutes = hh_mm_ss[1];
		final String hours = hh_mm_ss[0];
		final String seconds = hh_mm_ss[2];

		final String cron;
		switch ( periodicalTask.getPeriodUnit() ) {
			case SECOND:
					cron = String.format( "0/%s * %s 1/1 * ? *", period, getHours( periodicalTask ) );
				break;
			case MINUTE:
					cron = String.format( "%s 0/%s %s 1/1 * ? *", seconds, period, getHours( periodicalTask ) );
				break;
			case HOUR:
					cron = String.format( "%s %s 0/%s 1/1 * ? *", seconds, minutes, period );
				break;
			/*case DAY:
				cron = String.format( "%s %s %s 1/%s * ? *", seconds, minutes, hours, period );
				break;*/
			default:
				throw new IllegalArgumentException( String.format( "Illegal unit: %s", periodicalTask.getPeriodUnit() ) );
		}

		final String cronExpression = String.format( cron );

		log.debug( String.format( "Cron Expression: '%s'", cronExpression ) );

		return getCroneBuilder( cronExpression );
	}

	private String getHours( final PeriodicalExecutionTask periodicalTask ) {
		final List<String> executionHours = periodicalTask.getExecutionHours();
		if ( executionHours.size() == 24 ) {
			return "*";
		}

		return StringUtils.join( executionHours, "," );
	}
}
