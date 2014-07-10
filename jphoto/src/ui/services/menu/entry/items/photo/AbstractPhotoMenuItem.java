package ui.services.menu.entry.items.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItem;
import utils.UserUtils;

public abstract class AbstractPhotoMenuItem extends AbstractEntryMenuItem<Photo> {

	public static final String RELOAD_PHOTO_CALLBACK = "reloadPhotoCallback";

	public AbstractPhotoMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	protected User getPhotoAuthor() {
		return getUserService().load( menuEntry.getUserId() );
	}

	protected boolean isPhotoIsWithinAnonymousPeriod() {
		return getSecurityService().isPhotoAuthorNameMustBeHidden( menuEntry, accessor );
	}

	protected boolean isPhotoOfMenuAccessor() {
		return UserUtils.isUsersEqual( accessor, getUserService().load( menuEntry.getUserId() ) );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isPhotoOfMenuAccessor() && isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff();
	}

	protected GenreService getGenreService() {
		return services.getGenreService();
	}

	protected boolean isAccessorSeeingMenuOfOwnPhoto() {
		return UserUtils.isUsersEqual( accessor.getId(), menuEntry.getUserId() );
	}

	protected boolean isAccessorSuperAdmin() {
		return getSecurityService().isSuperAdminUser( accessor.getId() );
	}

	protected boolean isPhotoAuthorSuperAdmin() {
		return getSecurityService().isSuperAdminUser( menuEntry.getUserId() );
	}

}
