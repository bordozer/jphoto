package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.Map;

public class OnceExecutionTask extends AbstractExecutionTask {

	public OnceExecutionTask( final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.ONCE, dateUtilsService );
	}

	public OnceExecutionTask( final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final DateUtilsService dateUtilsService ) {
		super( ExecutionTaskType.ONCE, dateUtilsService );

		initTaskParameters( parametersMap );
	}

	@Override
	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {
		super.initTaskParameters( parametersMap );
	}

	@Override
	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		return super.getParametersMap();
	}

	@Override
	public String getDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( String.format( "Execution time: %s", dateUtilsService.formatDateTime( startTaskTime ) ) ).append( "<br />" );
//		builder.append( String.format( "Skip missed executions: %s", skipMissedExecutions ) ).append( "<br />" );

		return builder.toString();
	}
}
