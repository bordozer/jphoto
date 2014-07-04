package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;
import ui.services.MenuService;

public class AdminJobListBreadcrumbs extends AbstractBreadcrumb {

	public AdminJobListBreadcrumbs( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( MenuService.MAIN_MENU_ADMIN_JOBS, language );
	}
}
