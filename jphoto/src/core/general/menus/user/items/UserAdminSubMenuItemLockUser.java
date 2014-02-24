package core.general.menus.user.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.Services;
import utils.TranslatorUtils;

public class UserAdminSubMenuItemLockUser extends AbstractUserMenuItem {

	public UserAdminSubMenuItemLockUser( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Lock user: $1", menuEntry.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminLockUser( %d, '%s' ); return false;", menuEntry.getId(), menuEntry.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( isUserSuperAdmin() ) {
			return false;
		}

		if ( isUserCallingHisOwnMenu() ) {
			return false;
		}

		return isAccessorSuperAdmin();
	}

	@Override
	public String getMenuCssClass() {
		return ADMIN_MENU_ITEM_CSS_CLASS;
	}
}
