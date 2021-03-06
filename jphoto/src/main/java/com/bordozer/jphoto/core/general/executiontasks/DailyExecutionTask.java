package com.bordozer.jphoto.core.general.executiontasks;

import com.bordozer.jphoto.core.enums.SchedulerTaskProperty;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class DailyExecutionTask extends AbstractPeriodicalExecutionTask {

    private List<Weekday> weekdays;

    public DailyExecutionTask(final Services services) {
        super(ExecutionTaskType.DAILY, services);
    }

    public DailyExecutionTask(final Map<SchedulerTaskProperty, CommonProperty> parametersMap, final Services services) {
        super(ExecutionTaskType.DAILY, services);

        initTaskParameters(parametersMap);
    }

    @Override
    public void initTaskParameters(final Map<SchedulerTaskProperty, CommonProperty> parametersMap) {

        super.initTaskParameters(parametersMap);

        final CommonProperty property = parametersMap.get(SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS);
        final List<Weekday> weekdayList = newArrayList();
        for (final int weekday : property.getValueListInt()) {
            weekdayList.add(Weekday.getById(weekday));
        }

        weekdays = weekdayList;
    }

    @Override
    public Map<SchedulerTaskProperty, CommonProperty> getParametersMap() {
        final Map<SchedulerTaskProperty, CommonProperty> result = super.getParametersMap();

        result.put(SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS, new CommonProperty(SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS.getId(), CommonProperty.convertListToStringProperty(weekdays)));

        return result;
    }

    public List<Weekday> getWeekdays() {
        return weekdays;
    }

    @Override
    public TranslatableMessage getDescription() {

        final TranslatableMessage translatableMessage = new TranslatableMessage("ExecutionTask: Start date time", services).string(":").worldSeparator();
        translatableMessage.dateTimeFormatted(startTaskTime);
        translatableMessage.string("<br />");

        translatableMessage.translatableString("ExecutionTask: Week days").string(":").worldSeparator();
        if (weekdays.size() == 7) {
            translatableMessage.translatableString("ExecutionTask: Whole week");
        } else {
            final List<String> cronWeeks = newArrayList();
            for (final Weekday weekday : weekdays) {
                cronWeeks.add(weekday.getCroneSchedulerName());
            }
            translatableMessage.string(StringUtils.join(cronWeeks, ","));
        }
        translatableMessage.string("<br />");

        if (endTaskTime != null) {
            translatableMessage.translatableString("ExecutionTask: End time").string(":");
            translatableMessage.dateTimeFormatted(endTaskTime);
        }

        return translatableMessage;
    }
}
