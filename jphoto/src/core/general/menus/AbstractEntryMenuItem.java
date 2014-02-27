package core.general.menus;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.Services;
import core.services.user.UserService;
import org.apache.commons.lang.StringUtils;
import utils.UserUtils;

public abstract class AbstractEntryMenuItem<T extends PopupMenuAssignable> {

	public static final String ADMIN_SUB_MENU_ENTRY_TEXT = "ADMIN";
	public static final String ADMIN_SUB_MENU_ENTRY_COMMAND = "return false;";

	public static final String MENU_ITEM_CSS_CLASS_ADMIN = "adminMenuItem";
	public static final String MENU_ITEM_CSS_CLASS_DEFAULT = StringUtils.EMPTY;

	public static final int MENU_ITEM_HEIGHT = 27;
	public static final int MENU_SEPARATOR_HEIGHT = 3;

	protected final T menuEntry;
	protected final User accessor;

	protected final Services services;

	public abstract EntryMenuOperationType getEntryMenuType();

	public abstract AbstractEntryMenuItemCommand<T> getMenuItemCommand();

	public abstract boolean isAccessibleFor();

	public AbstractEntryMenuItem( final T menuEntry, final User accessor, final Services services ) {
		this.menuEntry = menuEntry;
		this.accessor = accessor;
		this.services = services;
	}

	public boolean isSubMenu() {
		return getEntryMenuType().isSubMenu();
	}

	public int getHeight() {
		return MENU_ITEM_HEIGHT;
	}

	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_DEFAULT;
	}

	public String getCommandIcon() {
		return String.format( "menus/%s", getEntryMenuType().getIcon() );
	}

	final protected boolean isUserWhoIsCallingMenuLogged() {
		return UserUtils.isLoggedUser( accessor );
	}

	final protected boolean isSuperAdminUser( final int userId ) {
		return services.getSecurityService().isSuperAdminUser( userId );
	}

	final protected boolean isSuperAdminUser( final User user ) {
		return isSuperAdminUser( user.getId() );
	}

	protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
		return services.getConfigurationService().getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES );
	}

	protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() {
		return ! isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn();
	}

	@Override
	public String toString() {
		return String.format( "Entry menu: %s", getMenuItemCommand() != null ? getMenuItemCommand().getMenuText() : getEntryMenuType() );
	}

	protected PhotoService getPhotoService() {
		return services.getPhotoService();
	}

	protected UserService getUserService() {
		return services.getUserService();
	}

	protected FavoritesService getFavoritesService() {
		return services.getFavoritesService();
	}

	protected int getId() {
		return menuEntry.getId();
	}

	protected SecurityService getSecurityService() {
		return services.getSecurityService();
	}
}
