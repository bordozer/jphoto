package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.enums.SchedulerTaskProperty;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.services.system.Services;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public abstract class AbstractPeriodicalExecutionTask extends AbstractExecutionTask {

    protected Date endTaskTime;

    protected AbstractPeriodicalExecutionTask(final ExecutionTaskType taskType, final Services services) {
        super(taskType, services);
    }

    @Override
    public void initTaskParameters(final Map<SchedulerTaskProperty, CommonProperty> parametersMap) {
        super.initTaskParameters(parametersMap);

        final CommonProperty property = parametersMap.get(SchedulerTaskProperty.PROPERTY_END_TASK_DATE);
        if (property != null) {
            endTaskTime = property.getValueTime(getDateUtilsService());
        }
    }

    @Override
    public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
        final Map<SchedulerTaskProperty, CommonProperty> parametersMap = super.getParametersMap();

        if (endTaskTime != null) {
            parametersMap.put(SchedulerTaskProperty.PROPERTY_END_TASK_DATE, new CommonProperty(SchedulerTaskProperty.PROPERTY_END_TASK_DATE.getId(), endTaskTime, getDateUtilsService()));
        } else {
            parametersMap.put(SchedulerTaskProperty.PROPERTY_END_TASK_DATE, new CommonProperty(SchedulerTaskProperty.PROPERTY_END_TASK_DATE.getId(), StringUtils.EMPTY));
        }

        return parametersMap;
    }

    public Date getEndTaskTime() {
        return endTaskTime;
    }
}
