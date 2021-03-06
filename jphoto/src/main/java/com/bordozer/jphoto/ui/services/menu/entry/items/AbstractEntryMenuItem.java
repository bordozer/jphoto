package com.bordozer.jphoto.ui.services.menu.entry.items;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractEntryMenuItem<T extends PopupMenuAssignable> {

    public static final String ADMIN_SUB_MENU_ENTRY_TEXT = "ADMIN submenu text";
    public static final String ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ENTRY_TEXT = "ADMIN submenu: Move to genre";
    public static final String ADMIN_SUB_MENU_ENTRY_COMMAND = StringUtils.EMPTY;

    public static final String MENU_ITEM_CSS_CLASS = "context-menu-item";
    public static final String MENU_ITEM_DISABLED_CSS_CLASS = "context-menu-item-disabled";
    public static final String MENU_ITEM_WITH_SUBMENU_CSS_CLASS = "context-menu-item-with-submenu";
    public static final String MENU_ITEM_ADMIN_CSS_CLASS = "context-menu-item-com.bordozer.jphoto.admin";

    public static final int MENU_ITEM_HEIGHT = 30;
    public static final int MENU_SEPARATOR_HEIGHT = 3;

    protected final T menuEntry;
    protected final User accessor;

    protected final Services services;

    public abstract EntryMenuOperationType getEntryMenuType();

    public abstract AbstractEntryMenuItemCommand<T> getMenuItemCommand();

    public abstract boolean isAccessibleFor();

    public AbstractEntryMenuItem(final T menuEntry, final User accessor, final Services services) {
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
        return MENU_ITEM_CSS_CLASS;
    }

    public String getCommandIcon() {
        return String.format("menus/%s", getEntryMenuType().getIcon());
    }

    public String getCallbackMessage() {
        return StringUtils.EMPTY;
    }

    final protected boolean isMenuAccessorLogged() {
        return UserUtils.isLoggedUser(accessor);
    }

    final protected boolean isSuperAdminUser(final int userId) {
        return services.getSecurityService().isSuperAdminUser(userId);
    }

    final protected boolean isSuperAdminUser(final User user) {
        return isSuperAdminUser(user.getId());
    }

    protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
        return services.getConfigurationService().getBoolean(ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES);
    }

    protected boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() {
        return !isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn();
    }

    @Override
    public String toString() {
        return String.format("Entry menu: %s", getMenuItemCommand() != null ? getMenuItemCommand().getMenuText() : getEntryMenuType());
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

    protected String getGenreNameTranslated(final Genre genre) {
        return services.getTranslatorService().translateGenre(genre, accessor.getLanguage());
    }

    protected Language getLanguage() {
        return accessor.getLanguage();
    }

    protected String translate(final String dd) {
        return services.getTranslatorService().translate(dd, getLanguage());
    }
}
