package admin.upgrade.tasks;

import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.services.services.UpgradeMonitor;
import core.exceptions.UpgradeException;
import core.services.dao.PhotoCommentDaoImpl;

public class PhotoCommentsReadMarkUpgradeTask extends AbstractUpgradeTask {

	@Override
	public void performUpgrade( final UpgradeMonitor upgradeMonitor ) throws UpgradeException {

		final UpgradeTaskToPerform currentTask = getCurrentTask( upgradeMonitor );

		String sql = String.format( "ALTER TABLE %s ADD COLUMN readtime BIGINT(20) DEFAULT 0;", PhotoCommentDaoImpl.TABLE_COMMENTS );

		sqlUtilsService.execSQL( sql );

		sql = String.format( "UPDATE TABLE %s SET %s = 0;", PhotoCommentDaoImpl.TABLE_COMMENTS, PhotoCommentDaoImpl.TABLE_COLUMN_READ_TIME );

		sqlUtilsService.execSQL( sql );

		upgradeMonitor.addTaskMessage( currentTask, sql );
	}

	@Override
	public String getDescription() {
		return String.format( "Photo Comments Read Mark" );
	}
}
