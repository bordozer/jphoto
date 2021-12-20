package com.bordozer.jphoto.ui.services.menu.entry.items.user.admin;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.AbstractUserMenuItem;

public class UserAdminSubMenuItemLockUser extends AbstractUserMenuItem {

    public UserAdminSubMenuItemLockUser(final User user, final User accessor, final Services services) {
        super(user, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
    }

    @Override
    public AbstractEntryMenuItemCommand<User> getMenuItemCommand() {

        return new AbstractEntryMenuItemCommand<User>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("Restrict user: $1", getLanguage(), menuEntry.getNameEscaped());
            }

            @Override
            public String getMenuCommand() {
                return String.format("adminRestrictUser( %d, '%s' );", menuEntry.getId(), menuEntry.getNameEscaped());
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (isUserSuperAdmin()) {
            return false;
        }

        if (!isAccessorSuperAdmin()) {
            return false;
        }

        return !isUserCallingHisOwnMenu();
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }
}
