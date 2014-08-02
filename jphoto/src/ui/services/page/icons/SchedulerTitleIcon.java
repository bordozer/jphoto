package ui.services.page.icons;

import core.services.system.Services;

public class SchedulerTitleIcon extends AbstractTitleIcon {

	public SchedulerTitleIcon( final Services services ) {
		super( services );
	}

	@Override
	protected String getIconPath() {
		return "scheduler/SchedulerIsStopped.png";
	}

	@Override
	protected String getIconTitle() {
		return getTranslatorService().translate( ! services.getSystemVarsService().isSchedulerEnabled() ? "The scheduled is disabled!" : "The scheduler is stopped!", getLanguage() );
	}

	@Override
	protected String getIconUrl() {
		return getUrlUtilsService().getAdminSchedulerTaskListLink();
	}
}
