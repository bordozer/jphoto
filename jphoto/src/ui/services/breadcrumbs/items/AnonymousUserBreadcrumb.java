package ui.services.breadcrumbs.items;

import core.general.configuration.ConfigurationKey;
import core.services.system.Services;
import core.services.translator.Language;

public class AnonymousUserBreadcrumb extends AbstractBreadcrumb {

	public AnonymousUserBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return services.getConfigurationService().getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
	}
}
