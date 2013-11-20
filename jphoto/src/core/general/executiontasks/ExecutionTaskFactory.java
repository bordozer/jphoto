package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;

import java.util.Map;

public class ExecutionTaskFactory {

	public static AbstractExecutionTask createInstance( final ExecutionTaskType executionTaskType, final DateUtilsService dateUtilsService ) {

		switch ( executionTaskType ) {
			case ONCE:
				return new OnceExecutionTask( dateUtilsService );
			case DAILY:
				return new DailyExecutionTask( dateUtilsService );
			case MONTHLY:
				return new MonthlyExecutionTask( dateUtilsService );
			case PERIODICAL:
				return new PeriodicalExecutionTask( dateUtilsService );
		}

		throw new IllegalArgumentException( String.format( "Invalid index %s", executionTaskType ) );
	}

	public static AbstractExecutionTask createInstance( final ExecutionTaskType executionTaskType, final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final DateUtilsService dateUtilsService ) {

		switch ( executionTaskType ) {
			case ONCE:
				return new OnceExecutionTask( parametersMap, dateUtilsService );
			case DAILY:
				return new DailyExecutionTask( parametersMap, dateUtilsService );
			case MONTHLY:
				return new MonthlyExecutionTask( parametersMap, dateUtilsService );
			case PERIODICAL:
				return new PeriodicalExecutionTask( parametersMap, dateUtilsService );
		}

		throw new IllegalArgumentException( String.format( "Invalid index %s", executionTaskType ) );
	}
}
