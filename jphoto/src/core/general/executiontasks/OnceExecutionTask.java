package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;

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
	public TranslatableMessage getDescription() {
		return new TranslatableMessage( "ExecutionTask: Execution time: $1", services ).dateTimeFormatted( startTaskTime ).string( "<br />" );
	}
}
