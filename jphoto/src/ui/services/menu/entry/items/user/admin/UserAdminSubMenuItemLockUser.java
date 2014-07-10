package ui.services.menu.entry.items.user.admin;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.user.AbstractUserMenuItem;

public class UserAdminSubMenuItemLockUser extends AbstractUserMenuItem {

	public UserAdminSubMenuItemLockUser( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
	}

	@Override
	public AbstractEntryMenuItemCommand<User> getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand<User>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Lock member: $1", getLanguage(), menuEntry.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminLockUser( %d, '%s' );", menuEntry.getId(), menuEntry.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( isUserSuperAdmin() ) {
			return false;
		}

		if ( ! isAccessorSuperAdmin() ) {
			return false;
		}

		return ! isUserCallingHisOwnMenu();
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}
