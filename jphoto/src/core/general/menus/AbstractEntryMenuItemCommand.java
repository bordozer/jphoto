package core.general.menus;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

public abstract class AbstractEntryMenuItemCommand<T extends PopupMenuAssignable> {

	protected final T menuEntry;
	protected User accessor;
	protected Services services;

	public AbstractEntryMenuItemCommand( final T menuEntry, final User accessor, final Services services ) {
		this.menuEntry = menuEntry;
		this.accessor = accessor;
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

	protected Language getLanguage() {
		return accessor.getLanguage();
	}
}
