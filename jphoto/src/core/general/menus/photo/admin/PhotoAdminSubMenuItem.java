package core.general.menus.photo.admin;

import core.general.menus.*;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoAdminSubMenuItem extends AbstractPhotoMenuItem {

	private final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
		EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT
		, EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE
		, EntryMenuOperationType.SEPARATOR
		, EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER
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
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( ADMIN_SUB_MENU_ENTRY_TEXT );
			}

			@Override
			public String getMenuCommand() {
				return ADMIN_SUB_MENU_ENTRY_COMMAND;
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( !isAccessorSuperAdmin() ) {
			return false;
		}

		return !isAccessorSeeingMenuOfOwnPhoto();
	}

	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus() );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getPhotoMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
