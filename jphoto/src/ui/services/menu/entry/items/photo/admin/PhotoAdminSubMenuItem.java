package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.*;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoAdminSubMenuItem extends AbstractPhotoMenuItem implements SubmenuAccesible {

	private final List<EntryMenuData> entryMenuOperationTypes = newArrayList(
		new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE )
		, new EntryMenuData( EntryMenuOperationType.SEPARATOR )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU )
		, new EntryMenuData( EntryMenuOperationType.SEPARATOR )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_NUDE_CONTENT_SET )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE )
		, new EntryMenuData( EntryMenuOperationType.SEPARATOR )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_GENERATE_PREVIEW )
		, new EntryMenuData( EntryMenuOperationType.SEPARATOR )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_PHOTO )
	);

	public PhotoAdminSubMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( ADMIN_SUB_MENU_ENTRY_TEXT, getLanguage() );
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
	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.PHOTO, getSubMenus(), getLanguage(), services );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_WITH_SUBMENU_CSS_CLASS;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getPhotoMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
