package admin.services.services;

import admin.upgrade.entities.UpgradeTaskLogEntry;
import admin.upgrade.entities.UpgradeTaskResult;
import admin.upgrade.entities.UpgradeTaskToPerform;
import admin.upgrade.tasks.AbstractUpgradeTask;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TranslatorUtils;

import java.util.List;

public class UpgradeServiceImpl implements UpgradeService {

	private final UpgradeMonitor upgradeMonitor = new UpgradeMonitor();

	@Autowired
	private UpgradeDao upgradeDao;

	@Autowired
	private SqlUtilsService sqlUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	private LogHelper log = new LogHelper( UpgradeServiceImpl.class );

	@Override
	public void performUpgrade( final List<UpgradeTaskToPerform> upgradeTasksToPerform ) {

		upgradeMonitor.setUpgradeTasksToPerform( upgradeTasksToPerform );
		upgradeMonitor.getTaskMessageMap().clear();

		try {
			for ( final UpgradeTaskToPerform upgradeTaskToPerform : upgradeTasksToPerform ) {

				upgradeMonitor.setCurrentTask( upgradeTaskToPerform );
				upgradeMonitor.setCurrentTaskProgress( 0 );
				upgradeMonitor.setCurrentTaskTotal( 1 );

				performUpgradeTask( upgradeTaskToPerform );

				upgradeDao.save( upgradeTaskToPerform ); // TODO: uncomment saving!

				if ( upgradeMonitor.getUpgradeState() == UpgradeState.ERROR ) {
					break;
				}
			}
//		} catch ( final Throwable t ) {
//			upgradeMonitor.setUpgradeState( UpgradeState.ERROR );
//			log.error( t );
		} finally {
			if ( upgradeMonitor.getUpgradeState() != UpgradeState.ERROR ) {
				upgradeMonitor.setUpgradeState( UpgradeState.STOPPED );
				upgradeMonitor.setCurrentTask( null );
			}
		}
	}

	private void performUpgradeTask( final UpgradeTaskToPerform upgradeTaskToPerform ) {

		try {
			upgradeTaskToPerform.setStartTime( dateUtilsService.getCurrentTime() );

			final AbstractUpgradeTask upgradeTask = upgradeTaskToPerform.getUpgradeTask();
			upgradeTask.setSqlUtilsService( sqlUtilsService );

			upgradeMonitor.addTaskMessage( upgradeTaskToPerform, String.format( "<span style=\"color: navy\"><b>%s</b></span>", TranslatorUtils.translate( "Start $1", upgradeTask.getDescription() ) ) );

			upgradeTask.performUpgrade( upgradeMonitor );

			upgradeTaskToPerform.setEndTimeTime( dateUtilsService.getCurrentTime() );
			upgradeTaskToPerform.setUpgradeTaskResult( UpgradeTaskResult.SUCCESSFUL );

			sqlUtilsService.execSQL( "COMMIT;" );

			upgradeMonitor.addTaskMessage( upgradeTaskToPerform, String.format( "<span style=\"color: green\"><b>%s</b></span>", TranslatorUtils.translate( "ALL UPGRADE TASKS HAVE BEEN PERFORMED SUCCESSFULLY" ) ) );
		} catch ( Throwable e ) {
			upgradeMonitor.setUpgradeState( UpgradeState.ERROR );
			upgradeTaskToPerform.setUpgradeTaskResult( UpgradeTaskResult.ERROR );
			upgradeMonitor.addTaskMessage( upgradeTaskToPerform, String.format( "<span style=\"color: red\"><b>%s:</b></span> %s", TranslatorUtils.translate( "ERROR" ), e.getMessage() ) );
			log.error( e );

			sqlUtilsService.execSQL( "ROLLBACK;" );
		}
	}

	@Override
	public UpgradeMonitor getUpgradeMonitor() {
		return upgradeMonitor;
	}

	@Override
	public List<UpgradeTaskLogEntry> getPerformedUpgradeTasks() {
		return upgradeDao.getPerformedUpgradeTasks();
	}
}
