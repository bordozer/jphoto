package core.general.menus.photo;

import core.general.menus.AbstractEntryMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractPhotoMenuItem extends AbstractEntryMenuItem<Photo> {

	public AbstractPhotoMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	public boolean isAccessibleFor() {
		return isSuperAdminUser( accessor ) || ! isPhotoIsWithinAnonymousPeriod();
	}

	protected User getPhotoAuthor() {
		return getUserService().load( menuEntry.getUserId() );
	}

	protected boolean isPhotoIsWithinAnonymousPeriod() {
		return getSecurityService().isPhotoAuthorNameMustBeHidden( menuEntry, accessor );
	}

	protected boolean isPhotoOfMenuCaller() {
		return UserUtils.isUsersEqual( accessor, getUserService().load( menuEntry.getUserId() ) );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isPhotoOfMenuCaller() && isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff();
	}

	protected GenreService getGenreService() {
		return services.getGenreService();
	}

	protected boolean isAccessorSeeingMenuOfOwnPhoto() {
		return UserUtils.isUsersEqual( accessor.getId(), menuEntry.getUserId() );
	}
}
