package core.general.executiontasks;

import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public abstract class AbstractExecutionTask {

	private final ExecutionTaskType taskType;

	protected Date startTaskTime;

	protected boolean skipMissedExecutions;

	protected boolean executionTaskActive;

	protected final Services services;

	public abstract String getDescription();

	public AbstractExecutionTask( final ExecutionTaskType taskType, final Services services ) {
		this.taskType = taskType;
		this.services = services;
	}

	public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = newLinkedHashMap();

		parametersMap.put( SchedulerTaskProperty.PROPERTY_START_TASK_DATE, new CommonProperty( SchedulerTaskProperty.PROPERTY_START_TASK_DATE.getId(), startTaskTime, getDateUtilsService() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS, new CommonProperty( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS.getId(), skipMissedExecutions ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_IS_ACTIVE, new CommonProperty( SchedulerTaskProperty.PROPERTY_IS_ACTIVE.getId(), executionTaskActive ) );

		return parametersMap;
	}

	public void initTaskParameters( final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {
		startTaskTime = parametersMap.get( SchedulerTaskProperty.PROPERTY_START_TASK_DATE ).getValueTime( getDateUtilsService() );
		skipMissedExecutions = parametersMap.get( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS ).getValueBoolean();
		executionTaskActive = parametersMap.get( SchedulerTaskProperty.PROPERTY_IS_ACTIVE ).getValueBoolean();
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

	public boolean isExecutionTaskActive() {
		return executionTaskActive;
	}

	public void setExecutionTaskActive( final boolean executionTaskActive ) {
		this.executionTaskActive = executionTaskActive;
	}

	protected DateUtilsService getDateUtilsService() {
		return services.getDateUtilsService();
	}

	protected TranslatorService getTranslatorService() {
		return services.getTranslatorService();
	}
}
