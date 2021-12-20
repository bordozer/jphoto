package com.bordozer.jphoto.admin.upgrade.entities;

import java.util.Date;

public class UpgradeTaskLogEntry {

    private String upgradeTaskName;
    private Date performanceTime;
    private UpgradeTaskResult upgradeTaskResult;

    public String getUpgradeTaskName() {
        return upgradeTaskName;
    }

    public void setUpgradeTaskName(final String upgradeTaskName) {
        this.upgradeTaskName = upgradeTaskName;
    }

    public Date getPerformanceTime() {
        return performanceTime;
    }

    public void setPerformanceTime(final Date performanceTime) {
        this.performanceTime = performanceTime;
    }

    public UpgradeTaskResult getUpgradeTaskResult() {
        return upgradeTaskResult;
    }

    public void setUpgradeTaskResult(final UpgradeTaskResult upgradeTaskResult) {
        this.upgradeTaskResult = upgradeTaskResult;
    }
}
