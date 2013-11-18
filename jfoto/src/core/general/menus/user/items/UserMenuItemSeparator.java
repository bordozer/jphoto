package core.general.menus.user.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import org.apache.commons.lang.StringUtils;

public class UserMenuItemSeparator extends AbstractUserMenuItem {

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEPARATOR;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int userId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
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
	public boolean isAccessibleForUser( final User user, final User userWhoIsCallingMenu ) {
		return true;
	}
}
