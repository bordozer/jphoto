package com.bordozer.jphoto.admin.services.services;

import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskLogEntry;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskResult;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.admin.upgrade.tasks.AbstractUpgradeTask;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("upgradeService")
public class UpgradeServiceImpl implements UpgradeService {

    private final UpgradeMonitor upgradeMonitor = new UpgradeMonitor();

    @Autowired
    private UpgradeDao upgradeDao;

    @Autowired
    private SqlUtilsService sqlUtilsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    private LogHelper log = new LogHelper();

    @Override
    public void performUpgrade(final List<UpgradeTaskToPerform> upgradeTasksToPerform, final User accessor) {

        upgradeMonitor.setUpgradeTasksToPerform(upgradeTasksToPerform);
        upgradeMonitor.getTaskMessageMap().clear();

        try {
            for (final UpgradeTaskToPerform upgradeTaskToPerform : upgradeTasksToPerform) {

                upgradeMonitor.setCurrentTask(upgradeTaskToPerform);
                upgradeMonitor.setCurrentTaskProgress(0);
                upgradeMonitor.setCurrentTaskTotal(1);

                performUpgradeTask(upgradeTaskToPerform, accessor);

                upgradeDao.save(upgradeTaskToPerform); // TODO: uncomment saving!

                if (upgradeMonitor.getUpgradeState() == UpgradeState.ERROR) {
                    break;
                }
            }
            //		} catch ( final Throwable t ) {
            //			upgradeMonitor.setUpgradeState( UpgradeState.ERROR );
            //			log.error( t );
        } finally {
            if (upgradeMonitor.getUpgradeState() != UpgradeState.ERROR) {
                upgradeMonitor.setUpgradeState(UpgradeState.STOPPED);
                upgradeMonitor.setCurrentTask(null);
            }
        }
    }

    private void performUpgradeTask(final UpgradeTaskToPerform upgradeTaskToPerform, final User accessor) {

        try {
            upgradeTaskToPerform.setStartTime(dateUtilsService.getCurrentTime());

            final AbstractUpgradeTask upgradeTask = upgradeTaskToPerform.getUpgradeTask();
            upgradeTask.setSqlUtilsService(sqlUtilsService);

            upgradeMonitor.addTaskMessage(upgradeTaskToPerform, String.format("<span style=\"color: navy\"><b>%s</b></span>"
                    , translatorService.translate("Start $1", accessor.getLanguage(), upgradeTask.getDescription())));

            upgradeTask.performUpgrade(upgradeMonitor);

            upgradeTaskToPerform.setEndTimeTime(dateUtilsService.getCurrentTime());
            upgradeTaskToPerform.setUpgradeTaskResult(UpgradeTaskResult.SUCCESSFUL);

            sqlUtilsService.execSQL("COMMIT;");

            upgradeMonitor.addTaskMessage(upgradeTaskToPerform, String.format("<span style=\"color: green\"><b>%s</b></span>"
                    , translatorService.translate("ALL UPGRADE TASKS HAVE BEEN PERFORMED SUCCESSFULLY", accessor.getLanguage())));
        } catch (Throwable e) {
            upgradeMonitor.setUpgradeState(UpgradeState.ERROR);
            upgradeTaskToPerform.setUpgradeTaskResult(UpgradeTaskResult.ERROR);
            upgradeMonitor.addTaskMessage(upgradeTaskToPerform, String.format("<span style=\"color: red\"><b>%s:</b></span> %s"
                    , translatorService.translate("ERROR", accessor.getLanguage()), e.getMessage()));
            log.error(e);

            sqlUtilsService.execSQL("ROLLBACK;");
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
