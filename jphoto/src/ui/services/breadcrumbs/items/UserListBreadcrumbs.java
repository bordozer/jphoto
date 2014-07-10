package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;
import ui.services.menu.main.MenuService;

public class UserListBreadcrumbs extends AbstractBreadcrumb {

	public UserListBreadcrumbs( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( MenuService.MAIN_MENU_MEMBERS, language );
	}
}
