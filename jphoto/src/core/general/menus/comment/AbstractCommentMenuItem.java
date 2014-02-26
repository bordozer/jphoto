package core.general.menus.comment;

import core.general.genre.Genre;
import core.general.menus.AbstractEntryMenuItem;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractCommentMenuItem extends AbstractEntryMenuItem<PhotoComment> {

	public AbstractCommentMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {
		return ! isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod();
	}

	protected Photo getPhoto() {
		return getPhotoService().load( menuEntry.getPhotoId() );
	}

	protected boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() {
		return getSecurityService().isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( menuEntry, accessor );
	}

	protected int minPhotosForMenu() {
		if ( isCommentAuthorOwnerOfThePhoto() ) {
			return 1;
		}

		return 0;
	}

	final protected boolean isCommentLeftByAccessor() {
		return UserUtils.isUsersEqual( menuEntry.getCommentAuthor(), accessor );
	}

	final protected boolean isAccessorOwnerOfTheThePhoto() {
		return getSecurityService().userOwnThePhoto( accessor, getPhoto() );
	}

	protected boolean isCommentAuthorOwnerOfThePhoto() {
		final Photo photo = getPhotoService().load( menuEntry.getPhotoId() );
		return UserUtils.isUsersEqual( photo.getUserId(), menuEntry.getCommentAuthor().getId() );
	}

	protected User getPhotoAuthor() {
		final Photo photo = getPhotoService().load( menuEntry.getPhotoId() );
		return getUserService().load( photo.getUserId() );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isCommentLeftByAccessor();
	}

	protected Genre getGenre( final PhotoComment photoComment ) {
		final int photoId = photoComment.getPhotoId();
		final Photo photo = getPhotoService().load( photoId );

		return services.getGenreService().load( photo.getGenreId() );
	}

	protected boolean isAccessorInTheBlackListOfCommentAuthor() {
		return getFavoritesService().isUserInBlackListOfUser( menuEntry.getCommentAuthor().getId(), accessor.getId() );
	}

	protected boolean isAccessorSuperAdmin() {
		return getSecurityService().isSuperAdminUser( accessor.getId() );
	}
}
