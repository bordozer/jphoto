package com.bordozer.jphoto.ui.services.page.icons;

import com.bordozer.jphoto.core.services.system.Services;

public class SchedulerTitleIcon extends AbstractTitleIcon {

    public SchedulerTitleIcon(final Services services) {
        super(services);
    }

    @Override
    protected String getIconPath() {
        return "scheduler/SchedulerIsStopped.png";
    }

    @Override
    protected String getIconTitle() {
//        return getTranslatorService().translate(!services.getSystemVarsService().isSchedulerEnabled() ? "The scheduled is disabled!" : "The scheduler is stopped!", getLanguage());
        return getTranslatorService().translate("The scheduler is stopped!", getLanguage()); // TODO: read scheduler enabled property
    }

    @Override
    protected String getIconUrl() {
        return getUrlUtilsService().getAdminSchedulerTaskListLink();
    }
}
