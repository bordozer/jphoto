package admin.upgrade.tasks;

import admin.services.services.SqlUtilsService;
import admin.services.services.UpgradeMonitor;
import admin.upgrade.entities.UpgradeTaskToPerform;
import core.exceptions.UpgradeException;

public abstract class AbstractUpgradeTask {

	protected SqlUtilsService sqlUtilsService;

	public abstract void performUpgrade( final UpgradeMonitor upgradeMonitor ) throws UpgradeException;

	public abstract String getDescription();

	public void setSqlUtilsService( final SqlUtilsService sqlUtilsService ) {
		this.sqlUtilsService = sqlUtilsService;
	}

	protected UpgradeTaskToPerform getCurrentTask( final UpgradeMonitor upgradeMonitor ) {
		return upgradeMonitor.getCurrentTask();
	}
}
