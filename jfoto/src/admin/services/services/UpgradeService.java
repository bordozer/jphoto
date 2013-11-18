package admin.services.services;

import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskToPerform;

import java.util.List;

public interface UpgradeService {

	void performUpgrade( final List<UpgradeTaskToPerform> upgradeTasksToPerform );

	UpgradeMonitor getUpgradeMonitor();

	List<UpgradeTaskLogEntry> getPerformedUpgradeTasks();
}
