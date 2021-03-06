package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuData;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType;
import com.bordozer.jphoto.ui.services.menu.entry.items.SubmenuAccessible;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoAdminMovePhotoToGenreSubMenu extends AbstractPhotoMenuItem implements SubmenuAccessible {

    private final List<EntryMenuData> entryMenuDatas;

    public PhotoAdminMovePhotoToGenreSubMenu(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);

        entryMenuDatas = newArrayList();

        final List<Genre> genres = services.getGenreService().loadAllSortedByNameForLanguage(getLanguage());
        for (final Genre genre : genres) {

			/*if ( menuEntry.getGenreId() == genre.getId() ) {
				continue;
			}*/

            final EntryMenuData entryMenuData = new EntryMenuData(EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM);
            entryMenuData.setCustomObject(genre);
            entryMenuDatas.add(entryMenuData);
        }
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate(ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ENTRY_TEXT, getLanguage());
            }

            @Override
            public String getMenuCommand() {
                return ADMIN_SUB_MENU_ENTRY_COMMAND;
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {
        return isAccessorSuperAdmin();
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_WITH_SUBMENU_CSS_CLASS;
    }

    @Override
    public EntryMenu getEntrySubMenu() {
        return new EntryMenu(menuEntry, EntryMenuType.PHOTO, getSubMenus(), getLanguage(), services);
    }

    private List<? extends AbstractEntryMenuItem> getSubMenus() {
        return services.getEntryMenuService().getPhotoMenu(menuEntry, accessor, entryMenuDatas).getEntryMenuItems();
    }
}
