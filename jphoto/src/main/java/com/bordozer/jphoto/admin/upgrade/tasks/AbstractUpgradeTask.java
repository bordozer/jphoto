package com.bordozer.jphoto.admin.upgrade.tasks;

import com.bordozer.jphoto.admin.services.services.SqlUtilsService;
import com.bordozer.jphoto.admin.services.services.UpgradeMonitor;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.core.exceptions.UpgradeException;

public abstract class AbstractUpgradeTask {

    protected SqlUtilsService sqlUtilsService;

    public abstract void performUpgrade(final UpgradeMonitor upgradeMonitor) throws UpgradeException;

    public abstract String getDescription();

    public void setSqlUtilsService(final SqlUtilsService sqlUtilsService) {
        this.sqlUtilsService = sqlUtilsService;
    }

    protected UpgradeTaskToPerform getCurrentTask(final UpgradeMonitor upgradeMonitor) {
        return upgradeMonitor.getCurrentTask();
    }
}
