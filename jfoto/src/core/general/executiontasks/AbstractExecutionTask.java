package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public abstract class AbstractExecutionTask {

	private final ExecutionTaskType taskType;

	protected Date startTaskTime;

	protected boolean skipMissedExecutions;

	protected boolean suspended; // TODO: this had better place in ScheduleTask

	protected final DateUtilsService dateUtilsService;

	public abstract String getDescription();

	public AbstractExecutionTask( final ExecutionTaskType taskType, final DateUtilsService dateUtilsService ) {
		this.taskType = taskType;
		this.dateUtilsService = dateUtilsService;
	}

	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = newLinkedHashMap();

		parametersMap.put( SchedulerTaskProperty.PROPERTY_START_TASK_DATE, new CommonProperty( SchedulerTaskProperty.PROPERTY_START_TASK_DATE.getId(), startTaskTime, dateUtilsService ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS, new CommonProperty( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS.getId(), skipMissedExecutions ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_IS_SUSPENDED, new CommonProperty( SchedulerTaskProperty.PROPERTY_IS_SUSPENDED.getId(), suspended ) );

		return parametersMap;
	}

	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {
		startTaskTime = parametersMap.get( SchedulerTaskProperty.PROPERTY_START_TASK_DATE ).getValueTime( dateUtilsService );
		skipMissedExecutions = parametersMap.get( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS ).getValueBoolean();
		suspended = parametersMap.get( SchedulerTaskProperty.PROPERTY_IS_SUSPENDED ).getValueBoolean();
	}

	public ExecutionTaskType getTaskType() {
		return taskType;
	}

	public boolean isSkipMissedExecutions() {
		return skipMissedExecutions;
	}

	public void setSkipMissedExecutions( final boolean skipMissedExecutions ) {
		this.skipMissedExecutions = skipMissedExecutions;
	}

	public Date getStartTaskTime() {
		return startTaskTime;
	}

	public void setStartTaskTime( final Date startTaskTime ) {
		this.startTaskTime = startTaskTime;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended( final boolean suspended ) {
		this.suspended = suspended;
	}
}
