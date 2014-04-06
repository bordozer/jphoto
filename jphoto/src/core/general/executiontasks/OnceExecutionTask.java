package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.system.Services;

import java.util.Map;

public class OnceExecutionTask extends AbstractExecutionTask {

	public OnceExecutionTask( final Services services ) {
		super( ExecutionTaskType.ONCE, services );
	}

	public OnceExecutionTask( final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final Services services ) {
		super( ExecutionTaskType.ONCE, services );

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

		builder.append( getTranslatorService( "Execution time: $1", getDateUtilsService().formatDateTime( startTaskTime ) ) ).append( "<br />" );
//		builder.append( String.format( "Skip missed executions: %s", skipMissedExecutions ) ).append( "<br />" );

		return builder.toString();
	}
}
