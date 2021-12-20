package com.bordozer.jphoto.admin.upgrade.entities;

import com.bordozer.jphoto.admin.upgrade.tasks.AbstractUpgradeTask;

import java.util.Date;

public class UpgradeTaskToPerform {

    private final String upgradeTaskName;
    private final String version;

    private final AbstractUpgradeTask upgradeTask;

    private Date startTime;
    private Date endTimeTime;

    private UpgradeTaskResult upgradeTaskResult;

    public UpgradeTaskToPerform(final String upgradeTaskName, final String version, final AbstractUpgradeTask upgradeTask) {
        this.upgradeTaskName = upgradeTaskName;
        this.version = version;
        this.upgradeTask = upgradeTask;
    }

    public String getUpgradeTaskName() {
        return upgradeTaskName;
    }

    public String getVersion() {
        return version;
    }

    public AbstractUpgradeTask getUpgradeTask() {
        return upgradeTask;
    }

    public UpgradeTaskResult getUpgradeTaskResult() {
        return upgradeTaskResult;
    }

    public void setUpgradeTaskResult(final UpgradeTaskResult upgradeTaskResult) {
        this.upgradeTaskResult = upgradeTaskResult;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTimeTime() {
        return endTimeTime;
    }

    public void setEndTimeTime(final Date endTimeTime) {
        this.endTimeTime = endTimeTime;
    }
}
