package ui.services.menu.entry.items.user.admin;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.*;
import ui.services.menu.entry.items.user.AbstractUserMenuItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserAdminSubMenuItem extends AbstractUserMenuItem implements SubmenuAccesible {

	private final List<EntryMenuData> entryMenuOperationTypes = newArrayList( new EntryMenuData( EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER ) );

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

		if ( !isAccessorSuperAdmin() ) {
			return false;
		}

		return ! isUserCallingHisOwnMenu();
	}

	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.USER, getSubMenus(), getLanguage(), services );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getUserMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
