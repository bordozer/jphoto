package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.enums.SchedulerTaskProperty;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.services.system.Services;

import java.util.Map;

public class ExecutionTaskFactory {

    public static AbstractExecutionTask createInstance(final ExecutionTaskType executionTaskType, final Services services) {

        switch (executionTaskType) {
            case ONCE:
                return new OnceExecutionTask(services);
            case DAILY:
                return new DailyExecutionTask(services);
            case MONTHLY:
                return new MonthlyExecutionTask(services);
            case PERIODICAL:
                return new PeriodicalExecutionTask(services);
        }

        throw new IllegalArgumentException(String.format("Invalid index %s", executionTaskType));
    }

    public static AbstractExecutionTask createInstance(final ExecutionTaskType executionTaskType, final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final Services services) {

        switch (executionTaskType) {
            case ONCE:
                return new OnceExecutionTask(parametersMap, services);
            case DAILY:
                return new DailyExecutionTask(parametersMap, services);
            case MONTHLY:
                return new MonthlyExecutionTask(parametersMap, services);
            case PERIODICAL:
                return new PeriodicalExecutionTask(parametersMap, services);
        }

        throw new IllegalArgumentException(String.format("Invalid index %s", executionTaskType));
    }
}
