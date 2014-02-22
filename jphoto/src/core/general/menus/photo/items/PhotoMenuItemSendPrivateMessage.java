package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;
import utils.UserUtils;

public class PhotoMenuItemSendPrivateMessage extends AbstractPhotoMenuItem {

	public PhotoMenuItemSendPrivateMessage( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			private User photoAuthor = getPhotoAuthor( menuEntry );

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", photoAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), photoAuthor.getId(), photoAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor( final Photo photo, final User userWhoIsCallingMenu ) {

		if ( isSuperAdminUser( userWhoIsCallingMenu ) && ! UserUtils.isUsersEqual( userWhoIsCallingMenu, getPhotoAuthor( photo.getId() ) ) ) {
			return true;
		}

		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && ! hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu )
			   && isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && super.isAccessibleFor( photo, userWhoIsCallingMenu )
			   && ! getFavoritesService().isUserInBlackListOfUser( photo.getUserId(), userWhoIsCallingMenu.getId() );

	}
}
