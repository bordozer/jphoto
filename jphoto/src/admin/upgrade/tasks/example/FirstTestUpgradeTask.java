package admin.upgrade.tasks.example;

import admin.services.services.UpgradeMonitor;
import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.upgrade.tasks.AbstractUpgradeTask;
import core.exceptions.UpgradeException;

public class FirstTestUpgradeTask extends AbstractUpgradeTask {

	@Override
	public void performUpgrade( final UpgradeMonitor upgradeMonitor ) throws UpgradeException {
		final UpgradeTaskToPerform currentTask = getCurrentTask( upgradeMonitor );

		upgradeMonitor.startTaskMessage( currentTask, "start FirstTestUpgradeTask" );

		final int currentTaskTotal = 3;

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

		upgradeMonitor.endTaskMessage( currentTask, "end FirstTestUpgradeTask" );
	}

	@Override
	public String getDescription() {
		return String.format( "FirstTestUpgradeTask description" );
	}
}
