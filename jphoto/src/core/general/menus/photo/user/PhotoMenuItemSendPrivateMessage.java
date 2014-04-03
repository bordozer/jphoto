package core.general.menus.photo.user;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

public class PhotoMenuItemSendPrivateMessage extends AbstractPhotoMenuItem {

	public PhotoMenuItemSendPrivateMessage( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {

			private User photoAuthor = getPhotoAuthor();

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Send private message to $1", getLanguage(), photoAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), photoAuthor.getId(), photoAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isMenuAccessorLogged() ) {
			return false;
		}

		if ( isAccessorSeeingMenuOfOwnPhoto() ) {
			return false;
		}

		if ( isSuperAdminUser( accessor ) ) {
			return true;
		}

		if ( isPhotoIsWithinAnonymousPeriod() ) {
			return false;
		}

		return ! getFavoritesService().isUserInBlackListOfUser( menuEntry.getUserId(), accessor.getId() );

	}
}
