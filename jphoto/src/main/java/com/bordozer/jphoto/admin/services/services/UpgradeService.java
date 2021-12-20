package com.bordozer.jphoto.admin.services.services;

import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskLogEntry;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.core.general.user.User;

import java.util.List;

public interface UpgradeService {

    void performUpgrade(final List<UpgradeTaskToPerform> upgradeTasksToPerform, final User accessor);

    UpgradeMonitor getUpgradeMonitor();

    List<UpgradeTaskLogEntry> getPerformedUpgradeTasks();
}
