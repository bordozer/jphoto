package admin.services.services;

import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskToPerform;
import core.general.user.User;

import java.util.List;

public interface UpgradeService {

	void performUpgrade( final List<UpgradeTaskToPerform> upgradeTasksToPerform, final User accessor );

	UpgradeMonitor getUpgradeMonitor();

	List<UpgradeTaskLogEntry> getPerformedUpgradeTasks();
}
