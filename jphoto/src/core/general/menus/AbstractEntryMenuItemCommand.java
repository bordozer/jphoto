package core.general.menus;

import core.services.security.Services;
import core.services.translator.TranslatorService;

public abstract class AbstractEntryMenuItemCommand<T extends PopupMenuAssignable> {

	protected final T menuEntry;
	protected Services services;

	public AbstractEntryMenuItemCommand( final T menuEntry, final Services services ) {
		this.menuEntry = menuEntry;
		this.services = services;
	}

	public abstract String getMenuText();

	public abstract String getMenuCommand();

	public int getId() {
		return menuEntry.getId();
	}

	protected TranslatorService getTranslatorService() {
		return services.getTranslatorService();
	}
}
