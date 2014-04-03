package ui.breadcrumbs;

import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

public abstract class AbstractBreadcrumb {

	protected final Services services;

	public abstract String getValue( final Language language );

	public AbstractBreadcrumb( final Services services ) {
		this.services = services;
	}

	protected EntityLinkUtilsService getEntityLinkUtilsService() {
		return services.getEntityLinkUtilsService();
	}

	protected TranslatorService getTranslatorService() {
		return services.getTranslatorService();
	}

	protected DateUtilsService getDateUtilsService() {
		return services.getDateUtilsService();
	}
}
