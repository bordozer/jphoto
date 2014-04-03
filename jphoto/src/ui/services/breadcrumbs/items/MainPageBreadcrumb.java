package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class MainPageBreadcrumb extends AbstractBreadcrumb {

	public MainPageBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, language );
	}
}
