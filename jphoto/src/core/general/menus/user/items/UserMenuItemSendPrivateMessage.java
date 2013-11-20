package core.general.menus.user.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import utils.TranslatorUtils;
import utils.UserUtils;

public class UserMenuItemSendPrivateMessage extends AbstractUserMenuItem {

	public static final String BEAN_NAME = "userMenuItemSendPrivateMessage";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int userId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			private final User user = userService.load( userId );

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", user.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", userWhoIsCallingMenu.getId(), userId, user.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleForUser( final User user, final User userWhoIsCallingMenu ) {
		if ( ! UserUtils.isUsersEqual( userWhoIsCallingMenu, user ) && isSuperAdminUser( userWhoIsCallingMenu ) ) {
			return true;
		}

		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && ! isMenuCallerIsSeeingOwnMenu( user, userWhoIsCallingMenu )
			   && ! favoritesService.isUserInBlackListOfUser( user.getId(), userWhoIsCallingMenu.getId() );
	}
}
