package admin.controllers.upgrade;

import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.services.services.UpgradeMonitor;
import core.general.base.AbstractGeneralModel;

import java.util.List;

public class UpgradeModel extends AbstractGeneralModel {

	private List<UpgradeTaskLogEntry> upgradeTaskLogEntries;
	private List<UpgradeTaskToPerform> upgradeTasksToPerform;

	private UpgradeMonitor upgradeMonitor;

	public List<UpgradeTaskLogEntry> getUpgradeTaskLogEntries() {
		return upgradeTaskLogEntries;
	}

	public void setUpgradeTaskLogEntries( final List<UpgradeTaskLogEntry> upgradeTaskLogEntries ) {
		this.upgradeTaskLogEntries = upgradeTaskLogEntries;
	}

	public List<UpgradeTaskToPerform> getUpgradeTasksToPerform() {
		return upgradeTasksToPerform;
	}

	public void setUpgradeTasksToPerform( final List<UpgradeTaskToPerform> upgradeTasksToPerform ) {
		this.upgradeTasksToPerform = upgradeTasksToPerform;
	}

	public UpgradeMonitor getUpgradeMonitor() {
		return upgradeMonitor;
	}

	public void setUpgradeMonitor( final UpgradeMonitor upgradeMonitor ) {
		this.upgradeMonitor = upgradeMonitor;
	}
}
