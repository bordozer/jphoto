package core.general.menus.user.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.user.User;
import core.services.system.Services;

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
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
