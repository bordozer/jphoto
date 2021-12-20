package com.bordozer.jphoto.admin.upgrade.tasks;

import com.bordozer.jphoto.admin.services.services.UpgradeMonitor;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.core.exceptions.UpgradeException;

public class PhotoVotingMaxAccessibleMarkUpgradeTask extends AbstractUpgradeTask {

    @Override
    public void performUpgrade(final UpgradeMonitor upgradeMonitor) throws UpgradeException {

        final UpgradeTaskToPerform currentTask = getCurrentTask(upgradeMonitor);

        String sql = "ALTER TABLE photoVoting ADD COLUMN maxAccessibleMark TINYINT(4);";

        sqlUtilsService.execSQL(sql);

        sql = "UPDATE TABLE photoVoting SET maxAccessibleMark = 0;";

        sqlUtilsService.execSQL(sql);

        upgradeMonitor.addTaskMessage(currentTask, sql);
    }

    @Override
    public String getDescription() {
        return String.format("Photo Voting Max Accessible Mark Upgrade Task");
    }
}
