package admin.upgrade.tasks.example;

import admin.services.services.UpgradeMonitor;
import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.upgrade.tasks.AbstractUpgradeTask;
import core.exceptions.UpgradeException;

public class SecondTestUpgradeTask extends AbstractUpgradeTask {

	@Override
	public void performUpgrade( final UpgradeMonitor upgradeMonitor ) throws UpgradeException {
		final UpgradeTaskToPerform currentTask = upgradeMonitor.getCurrentTask();

		upgradeMonitor.startTaskMessage( currentTask, "start SecondTestUpgradeTask" );

		final int currentTaskTotal = 5;

		upgradeMonitor.setCurrentTaskTotal( currentTaskTotal );

		for ( int i = 0; i < currentTaskTotal; i++ ) {
			try {
				Thread.sleep( 1000 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
			upgradeMonitor.addTaskMessage( currentTask, String.format( "Current step: %s", i ) );
			upgradeMonitor.increment();
		}

		upgradeMonitor.endTaskMessage( currentTask, "end SecondTestUpgradeTask" );
	}

	@Override
	public String getDescription() {
		return String.format( "SecondTestUpgradeTask description" );
	}
}
