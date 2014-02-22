package core.general.menus;

import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.Services;
import core.services.user.UserService;
import utils.UserUtils;

public abstract class AbstractEntryMenuItem<T extends PopupMenuAssignable> {

	public static final String COMPLAINT_MESSAGE_JS_FUNCTION = "sendComplaintMessage";

	public static final int MENU_ITEM_HEIGHT = 27;
	public static final int MENU_SEPARATOR_HEIGHT = 5;

	protected final T menuEntry;
	protected final User accessor;

	protected final Services services;

	public abstract EntryMenuOperationType getEntryMenuType();

	public abstract boolean isAccessibleFor( final T menuEntry, final User accessor );

	public abstract AbstractEntryMenuItemCommand getMenuItemCommand();

	public AbstractEntryMenuItem( final T menuEntry, final User accessor, final Services services ) {
		this.menuEntry = menuEntry;
		this.accessor = accessor;
		this.services = services;
	}

	public boolean isSubMenu() {
		return getEntryMenuType().isSubMenu();
	}

	final protected boolean isUserWhoIsCallingMenuLogged( final User userWhoIsCallingMenu ) {
		return UserUtils.isLoggedUser( userWhoIsCallingMenu );
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

	public int getHeight() {
		return MENU_ITEM_HEIGHT;
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
}
