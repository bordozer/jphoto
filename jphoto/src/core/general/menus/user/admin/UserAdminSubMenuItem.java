package core.general.menus.user.admin;

import core.general.menus.*;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.user.User;
import core.services.security.Services;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserAdminSubMenuItem extends AbstractUserMenuItem {

	private final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
		EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER
	);

	public UserAdminSubMenuItem( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU;
	}

	@Override
	public AbstractEntryMenuItemCommand<User> getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand<User>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( ADMIN_SUB_MENU_ENTRY_TEXT, accessor.getLanguage() );
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

		return ! isUserCallingHisOwnMenu();
	}

	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus(), getLanguage(), services );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getUserMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
