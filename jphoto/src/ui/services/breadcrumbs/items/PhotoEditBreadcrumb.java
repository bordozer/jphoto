package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class PhotoEditBreadcrumb extends AbstractBreadcrumb {

	public PhotoEditBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( "Edit", language );
	}
}
