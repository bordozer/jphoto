package core.general.menus.comment;

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
		return services.getSecurityService().userOwnThePhoto( accessor, getPhoto() );
	}

	private Photo getPhoto() {
		return getPhotoService().load( menuEntry.getPhotoId() );
	}

	final protected boolean isCommentLeftByUserWhoIsCallingMenu() {
		return UserUtils.isUsersEqual( menuEntry.getCommentAuthor(), accessor );
	}

	protected boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() {
		return services.getSecurityService().isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( menuEntry, accessor );
	}

	protected int minPhotosForMenu() {
		if ( isCommentAuthorOwnerOfPhoto() ) {
			return 1;
		}

		return 0;
	}

	protected boolean isCommentAuthorOwnerOfPhoto() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		final User photoAuthor = getPhotoAuthor();

		return UserUtils.isUsersEqual( photoAuthor, commentAuthor );
	}

	protected User getPhotoAuthor() {
		final Photo photo = getPhotoService().load( menuEntry.getPhotoId() );
		return getUserService().load( photo.getUserId() );
	}

	protected boolean isCommentOfMenuCaller() {
		return UserUtils.isUsersEqual( accessor, menuEntry.getCommentAuthor() );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isCommentOfMenuCaller();
	}
}
