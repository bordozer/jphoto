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

	final protected boolean isUserWhoIsCallingMenuOwnerOfThePhoto() {
		return getSecurityService().userOwnThePhoto( accessor, getPhoto() );
	}

	protected Photo getPhoto() {
		return getPhotoService().load( menuEntry.getPhotoId() );
	}

	final protected boolean isCommentLeftByUserWhoIsCallingMenu() {
		return UserUtils.isUsersEqual( menuEntry.getCommentAuthor(), accessor );
	}

	protected boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() {
		return getSecurityService().isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( menuEntry, accessor );
	}

	protected int minPhotosForMenu() {
		if ( isCommentAuthorOwnerOfPhoto() ) {
			return 1;
		}

		return 0;
	}

	protected boolean isCommentAuthorOwnerOfPhoto() {
		final Photo photo = getPhotoService().load( menuEntry.getPhotoId() );
		return UserUtils.isUsersEqual( photo.getUserId(), menuEntry.getCommentAuthor().getId() );
	}

	protected User getPhotoAuthor() {
		final Photo photo = getPhotoService().load( menuEntry.getPhotoId() );
		return getUserService().load( photo.getUserId() );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isCommentLeftByUserWhoIsCallingMenu();
	}

	protected User getCommentAuthor() {
		return menuEntry.getCommentAuthor();
	}

	protected Genre getGenre( final PhotoComment photoComment ) {
		final int photoId = photoComment.getPhotoId();
		final Photo photo = getPhotoService().load( photoId );

		return services.getGenreService().load( photo.getGenreId() );
	}
}
