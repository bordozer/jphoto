package core.services.translator.message;

import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

public abstract class AbstractTranslatableMessageParameter {

	protected final Services services;

	protected AbstractTranslatableMessageParameter( final Services services ) {
		this.services = services;
	}

	public abstract String getValue( final Language language );

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
