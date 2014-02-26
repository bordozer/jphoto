package core.general.menus.user.items;

import core.general.menus.*;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserAdminSubMenuItem extends AbstractUserMenuItem {

	final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
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
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "ADMIN" );
			}

			@Override
			public String getMenuCommand() {
				return "return false;";
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
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus() );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getUserMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
