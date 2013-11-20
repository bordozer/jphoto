package admin.services.services;

import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskToPerform;

import java.util.List;

public interface UpgradeDao {

	void save( final UpgradeTaskToPerform upgradeTaskToPerform );

	List<UpgradeTaskLogEntry> getPerformedUpgradeTasks();
}
