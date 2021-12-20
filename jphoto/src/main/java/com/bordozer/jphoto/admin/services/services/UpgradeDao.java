package com.bordozer.jphoto.admin.services.services;

import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskLogEntry;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;

import java.util.List;

public interface UpgradeDao {

    void save(final UpgradeTaskToPerform upgradeTaskToPerform);

    List<UpgradeTaskLogEntry> getPerformedUpgradeTasks();
}
