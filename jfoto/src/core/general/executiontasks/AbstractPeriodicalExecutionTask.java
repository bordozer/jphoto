package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

public abstract class AbstractPeriodicalExecutionTask extends AbstractExecutionTask {

	protected Date endTaskTime;

	protected AbstractPeriodicalExecutionTask( final ExecutionTaskType taskType, final DateUtilsService dateUtilsService ) {
		super( taskType, dateUtilsService );
	}

	@Override
	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {
		super.initTaskParameters( parametersMap );

		final CommonProperty property = parametersMap.get( SchedulerTaskProperty.PROPERTY_END_TASK_DATE );
		if ( property != null ) {
			endTaskTime = property.getValueTime( dateUtilsService );
		}
	}

	@Override
	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = super.getParametersMap();

		if ( endTaskTime != null ) {
			parametersMap.put( SchedulerTaskProperty.PROPERTY_END_TASK_DATE, new CommonProperty( SchedulerTaskProperty.PROPERTY_END_TASK_DATE.getId(), endTaskTime, dateUtilsService ) );
		} else {
			parametersMap.put( SchedulerTaskProperty.PROPERTY_END_TASK_DATE, new CommonProperty( SchedulerTaskProperty.PROPERTY_END_TASK_DATE.getId(), StringUtils.EMPTY ) );
		}

		return parametersMap;
	}

	public Date getEndTaskTime() {
		return endTaskTime;
	}
}
