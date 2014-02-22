package core.general.menus.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractPhotoMenuItem extends AbstractEntryMenuItem<Photo> {

	public AbstractPhotoMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	public boolean isAccessibleFor( final Photo photo, final User userWhoIsCallingMenu ) {
		return isSuperAdminUser( userWhoIsCallingMenu ) || ! isPhotoIsWithinAnonymousPeriod( photo, userWhoIsCallingMenu );
	}

	protected User getPhotoAuthor( final Photo photo ) {
		return getUserService().load( photo.getUserId() );
	}

	protected User getPhotoAuthor( final int photoId ) {
		return getUserService().load( getPhoto( photoId ).getUserId() );
	}

	protected Photo getPhoto( final int photoId ) {
		return getPhotoService().load( photoId );
	}

	protected boolean isPhotoIsWithinAnonymousPeriod( final Photo photo, final User userWhoIsCallingMenu ) {
		return services.getSecurityService().isPhotoAuthorNameMustBeHidden( photo, userWhoIsCallingMenu );
	}

	protected boolean isPhotoOfMenuCaller( final Photo photo, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( userWhoIsCallingMenu, getUserService().load( photo.getUserId() ) );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final Photo photo, final User userWhoIsCallingMenu ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isPhotoOfMenuCaller( photo, userWhoIsCallingMenu );
	}

	protected GenreService getGenreService() {
		return services.getGenreService();
	}
}
