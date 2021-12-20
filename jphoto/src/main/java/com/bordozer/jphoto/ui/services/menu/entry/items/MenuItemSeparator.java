package com.bordozer.jphoto.ui.services.menu.entry.items;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import org.apache.commons.lang3.StringUtils;

public class MenuItemSeparator<T extends PopupMenuAssignable> extends AbstractEntryMenuItem<T> {

    public static final String BEAN_NAME = "separatorMenuItem";

    public MenuItemSeparator(final T menuEntry, final User accessor, final Services services) {
        super(menuEntry, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.SEPARATOR;
    }

    @Override
    public boolean isAccessibleFor() {
        return true;
    }

    @Override
    public AbstractEntryMenuItemCommand<T> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<T>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return StringUtils.EMPTY;
            }

            @Override
            public String getMenuCommand() {
                return StringUtils.EMPTY;
            }
        };
    }

    @Override
    public int getHeight() {
        return MENU_SEPARATOR_HEIGHT;
    }
}
