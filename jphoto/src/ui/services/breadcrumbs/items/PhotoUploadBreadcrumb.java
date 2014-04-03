package ui.services.breadcrumbs.items;

import core.services.system.MenuService;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoUploadBreadcrumb extends AbstractBreadcrumb {

	public PhotoUploadBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( MenuService.MAIN_MENU_UPLOAD_PHOTO, language );
	}
}
