package com.bordozer.jphoto.admin.controllers.scheduler.tasks.list;

import com.bordozer.jphoto.core.general.scheduler.SchedulerTask;

public class ScheduledTaskData {

    private final SchedulerTask schedulerTasks;
    private boolean scheduled;

    public ScheduledTaskData(final SchedulerTask schedulerTasks) {
        this.schedulerTasks = schedulerTasks;
    }

    public SchedulerTask getSchedulerTasks() {
        return schedulerTasks;
    }

    public void setScheduled(final boolean scheduled) {
        this.scheduled = scheduled;
    }

    public boolean isScheduled() {
        return scheduled;
    }
}
