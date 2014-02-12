package core.general.menus.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import utils.UserUtils;

public abstract class AbstractPhotoMenuItem extends AbstractEntryMenuItem {

	protected abstract AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu );

	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		return isSuperAdminUser( userWhoIsCallingMenu ) || ! isPhotoIsWithinAnonymousPeriod( photo, userWhoIsCallingMenu );
	}

	protected User getPhotoAuthor( final int photoId ) {
		return userService.load( getPhoto( photoId ).getUserId() );
	}

	protected Photo getPhoto( final int photoId ) {
		return photoService.load( photoId );
	}

	protected boolean isPhotoIsWithinAnonymousPeriod( final Photo photo, final User userWhoIsCallingMenu ) {
		return securityService.isPhotoAuthorNameMustBeHidden( photo, userWhoIsCallingMenu );
	}

	protected boolean isPhotoOfMenuCaller( final Photo photo, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( userWhoIsCallingMenu, userService.load( photo.getUserId() ) );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final Photo photo, final User userWhoIsCallingMenu ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isPhotoOfMenuCaller( photo, userWhoIsCallingMenu );
	}
}
