package com.bordozer.jphoto.admin.controllers.scheduler.tasks.list;

import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;

import java.util.List;
import java.util.Map;

public class SchedulerTaskListModel extends AbstractGeneralModel {

    private Map<Integer, SavedJob> savedJobMap;

    private List<ScheduledTaskData> scheduledTasksData;

    private String[] schedulerTaskCheckbox;
    private String schedulerTasksFormAction;

    private boolean schedulerRunning;
    private boolean schedulerEnabled;

    public Map<Integer, SavedJob> getSavedJobMap() {
        return savedJobMap;
    }

    public void setSavedJobMap(final Map<Integer, SavedJob> savedJobMap) {
        this.savedJobMap = savedJobMap;
    }

    public List<ScheduledTaskData> getScheduledTasksData() {
        return scheduledTasksData;
    }

    public void setScheduledTasksData(final List<ScheduledTaskData> scheduledTasksData) {
        this.scheduledTasksData = scheduledTasksData;
    }

    public boolean isSchedulerRunning() {
        return schedulerRunning;
    }

    public void setSchedulerRunning(final boolean schedulerRunning) {
        this.schedulerRunning = schedulerRunning;
    }

    public String[] getSchedulerTaskCheckbox() {
        return schedulerTaskCheckbox;
    }

    public void setSchedulerTaskCheckbox(final String[] schedulerTaskCheckbox) {
        this.schedulerTaskCheckbox = schedulerTaskCheckbox;
    }

    public String getSchedulerTasksFormAction() {
        return schedulerTasksFormAction;
    }

    public void setSchedulerTasksFormAction(final String schedulerTasksFormAction) {
        this.schedulerTasksFormAction = schedulerTasksFormAction;
    }

    public boolean isSchedulerEnabled() {
        return schedulerEnabled;
    }

    public void setSchedulerEnabled(final boolean schedulerEnabled) {
        this.schedulerEnabled = schedulerEnabled;
    }
}
