package ui.services.page.icons;

import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.UrlUtilsService;
import ui.context.EnvironmentContext;

public abstract class AbstractTitleIcon {

	protected final Services services;

	protected abstract String getIconPath();

	protected abstract String getIconTitle();

	protected abstract String getIconUrl();

	protected AbstractTitleIcon( final Services services ) {
		this.services = services;
	}

	public final String getIcon() {
		return String.format( "<a href=\"%s\">%s</a>", getIconUrl(), getIconImage() );
	}

	private String getIconImage() {
		return String.format( "<img src=\"%s/%s\" height=\"16\" width=\"16\" title=\"%s\" />"
			, getUrlUtilsService().getSiteImagesPath()
			, getIconPath()
			, getIconTitle()
		);
	}

	protected UrlUtilsService getUrlUtilsService() {
		return services.getUrlUtilsService();
	}

	protected TranslatorService getTranslatorService() {
		return services.getTranslatorService();
	}

	protected Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
