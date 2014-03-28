package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

public abstract class AbstractTranslatableMessageUnit {

	protected final Services services;

	protected AbstractTranslatableMessageUnit( final Services services ) {
		this.services = services;
	}

	public abstract String translate( final Language language );

	protected TranslatorService getTranslatorService() {
		return services.getTranslatorService();
	}

	protected EntityLinkUtilsService getEntityLinkUtilsService() {
		return services.getEntityLinkUtilsService();
	}

	protected DateUtilsService getDateUtilsService() {
		return services.getDateUtilsService();
	}
}
