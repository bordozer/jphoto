package com.bordozer.jphoto.admin.services.scheduler.triggers;

import com.bordozer.jphoto.core.general.executiontasks.AbstractExecutionTask;
import com.bordozer.jphoto.core.general.executiontasks.DailyExecutionTask;
import com.bordozer.jphoto.core.general.executiontasks.Weekday;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.quartz.TriggerBuilder.newTrigger;

public class DailyJobTrigger extends AbstractJobTrigger {

    protected DailyJobTrigger(final int schedulerTaskId, final AbstractExecutionTask executionTask, final DateUtilsService dateUtilsService) {
        super(schedulerTaskId, executionTask, dateUtilsService);
    }

    @Override
    public Trigger createTrigger() {

        final DailyExecutionTask dailyTask = (DailyExecutionTask) executionTask;

        return newTrigger()
                .withIdentity(getTriggerIdentity(), getGroupIdentity())
                .startAt(dateUtilsService.extractDate(dailyTask.getStartTaskTime()))
                .endAt(getEndDate(dailyTask.getEndTaskTime()))
                .withSchedule(getCronSchedule())
                .build();
    }

    private CronScheduleBuilder getCronSchedule() {
        final DailyExecutionTask dailyTask = (DailyExecutionTask) executionTask;

        final LogHelper log = new LogHelper();

        final String[] hh_mm_ss = getCronTime();
        final String cronTime = String.format("%s %s %s", hh_mm_ss[2], hh_mm_ss[1], hh_mm_ss[0]);
        final String cronExpression = String.format("%s ? * %s", cronTime, getCronDaysOfWeek(dailyTask));

        log.debug(String.format("Cron Expression: '%s'", cronExpression));

        return getCroneBuilder(cronExpression);
    }

    private String getCronDaysOfWeek(final DailyExecutionTask dailyTask) {
        final Set<String> daysOfWeek = newHashSet();
        for (final Weekday weekday : dailyTask.getWeekdays()) {
            daysOfWeek.add(weekday.getCroneSchedulerName());
        }

        return StringUtils.join(daysOfWeek, ",");
    }
}
