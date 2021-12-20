package com.bordozer.jphoto.admin.upgrade.tasks;

import com.bordozer.jphoto.admin.services.services.UpgradeMonitor;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.core.exceptions.UpgradeException;
import com.bordozer.jphoto.core.services.dao.PhotoCommentDaoImpl;

public class PhotoCommentsReadMarkUpgradeTask extends AbstractUpgradeTask {

    @Override
    public void performUpgrade(final UpgradeMonitor upgradeMonitor) throws UpgradeException {

        final UpgradeTaskToPerform currentTask = getCurrentTask(upgradeMonitor);

        String sql = String.format("ALTER TABLE %s ADD COLUMN readtime BIGINT(20) DEFAULT 0;", PhotoCommentDaoImpl.TABLE_COMMENTS);

        sqlUtilsService.execSQL(sql);

        sql = String.format("UPDATE TABLE %s SET %s = 0;", PhotoCommentDaoImpl.TABLE_COMMENTS, PhotoCommentDaoImpl.TABLE_COLUMN_READ_TIME);

        sqlUtilsService.execSQL(sql);

        upgradeMonitor.addTaskMessage(currentTask, sql);
    }

    @Override
    public String getDescription() {
        return String.format("Photo Comments Read Mark");
    }
}
