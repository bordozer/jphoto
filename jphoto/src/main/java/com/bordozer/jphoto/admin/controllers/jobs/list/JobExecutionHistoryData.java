package com.bordozer.jphoto.admin.controllers.jobs.list;

import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry;
import com.bordozer.jphoto.core.general.scheduler.SchedulerTask;

public class JobExecutionHistoryData {

    private final JobExecutionHistoryEntry jobExecutionHistoryEntry;

    private SchedulerTask schedulerTask;

    public JobExecutionHistoryData(final JobExecutionHistoryEntry jobExecutionHistoryEntry) {
        this.jobExecutionHistoryEntry = jobExecutionHistoryEntry;
    }

    public JobExecutionHistoryEntry getJobExecutionHistoryEntry() {
        return jobExecutionHistoryEntry;
    }

    public SchedulerTask getSchedulerTask() {
        return schedulerTask;
    }

    public void setSchedulerTask(final SchedulerTask schedulerTask) {
        this.schedulerTask = schedulerTask;
    }
}
