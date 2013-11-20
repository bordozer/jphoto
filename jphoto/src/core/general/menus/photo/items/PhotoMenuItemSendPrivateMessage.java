package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import utils.TranslatorUtils;
import utils.UserUtils;

public class PhotoMenuItemSendPrivateMessage extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemSendPrivateMessage";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			private User photoAuthor = getPhotoAuthor( photoId );

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", photoAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", userWhoIsCallingMenu.getId(), photoAuthor.getId(), photoAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {

		if ( isSuperAdminUser( userWhoIsCallingMenu ) && ! UserUtils.isUsersEqual( userWhoIsCallingMenu, getPhotoAuthor( photo.getId() ) ) ) {
			return true;
		}

		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && ! hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu )
			   && isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && super.isAccessibleForPhoto( photo, userWhoIsCallingMenu )
			   && !favoritesService.isUserInBlackListOfUser( photo.getUserId(), userWhoIsCallingMenu.getId() );

	}
}
