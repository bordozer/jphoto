package core.general.menus.comment;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import utils.UserUtils;

public abstract class AbstractCommentMenuItem extends AbstractEntryMenuItem {

	protected abstract AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu );

	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return ! isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( photoComment, userWhoIsCallingMenu );
	}

	final protected boolean isUserWhoIsCallingMenuOwnerOfThePhoto( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return securityService.userOwnThePhoto( userWhoIsCallingMenu, getPhoto( photoComment ) );
	}

	private Photo getPhoto( final PhotoComment photoComment ) {
		return photoService.load( photoComment.getPhotoId() );
	}

	final protected boolean isCommentLeftByUserWhoIsCallingMenu( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( photoComment.getCommentAuthor(), userWhoIsCallingMenu );
	}

	protected User getCommentAuthor( final int commentId ) {
		final PhotoComment photoComment = photoCommentService.load( commentId );
		return photoComment.getCommentAuthor();
	}

	protected boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( photoComment, userWhoIsCallingMenu );
	}

	protected int minPhotosForMenu( final PhotoComment photoComment ) {
		if ( isCommentAuthorOwnerOfPhoto( photoComment ) ) {
			return 1;
		}

		return 0;
	}

	protected boolean isCommentAuthorOwnerOfPhoto( final PhotoComment photoComment ) {
		final User commentAuthor = photoComment.getCommentAuthor();

		final User photoAuthor = getPhotoAuthor( photoComment );

		return UserUtils.isUsersEqual( photoAuthor, commentAuthor );
	}

	protected User getPhotoAuthor( final PhotoComment photoComment ) {
		final Photo photo = photoService.load( photoComment.getPhotoId() );
		return userService.load( photo.getUserId() );
	}

	protected boolean isCommentOfMenuCaller( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( userWhoIsCallingMenu, photoComment.getCommentAuthor() );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isCommentOfMenuCaller( photoComment, userWhoIsCallingMenu );
	}
}
