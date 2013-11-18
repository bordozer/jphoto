package admin.upgrade.tasks;

import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.services.services.UpgradeMonitor;
import core.exceptions.UpgradeException;

public class PhotoVotingMaxAccessibleMarkUpgradeTask extends AbstractUpgradeTask {

	@Override
	public void performUpgrade( final UpgradeMonitor upgradeMonitor ) throws UpgradeException {

		final UpgradeTaskToPerform currentTask = getCurrentTask( upgradeMonitor );

		String sql = "ALTER TABLE photoVoting ADD COLUMN maxAccessibleMark TINYINT(4);";

		sqlUtilsService.execSQL( sql );

		sql = "UPDATE TABLE photoVoting SET maxAccessibleMark = 0;";

		sqlUtilsService.execSQL( sql );

		upgradeMonitor.addTaskMessage( currentTask, sql );
	}

	@Override
	public String getDescription() {
		return String.format( "Photo Voting Max Accessible Mark Upgrade Task" );
	}
}
