package core.general.menus.comment;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractCommentMenuItem extends AbstractEntryMenuItem<PhotoComment> {

	public AbstractCommentMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		return ! isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( photoComment, accessor );
	}

	final protected boolean isUserWhoIsCallingMenuOwnerOfThePhoto( final PhotoComment photoComment, final User accessor ) {
		return services.getSecurityService().userOwnThePhoto( accessor, getPhoto( photoComment ) );
	}

	private Photo getPhoto( final PhotoComment photoComment ) {
		return getPhotoService().load( photoComment.getPhotoId() );
	}

	final protected boolean isCommentLeftByUserWhoIsCallingMenu( final PhotoComment photoComment, final User accessor ) {
		return UserUtils.isUsersEqual( photoComment.getCommentAuthor(), accessor );
	}

	protected User getCommentAuthor( final int commentId ) {
		final PhotoComment photoComment = services.getPhotoCommentService().load( commentId );
		return photoComment.getCommentAuthor();
	}

	protected boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( final PhotoComment photoComment, final User accessor ) {
		return services.getSecurityService().isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( photoComment, accessor );
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
		final Photo photo = getPhotoService().load( photoComment.getPhotoId() );
		return getUserService().load( photo.getUserId() );
	}

	protected boolean isCommentOfMenuCaller( final PhotoComment photoComment, final User accessor ) {
		return UserUtils.isUsersEqual( accessor, photoComment.getCommentAuthor() );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final PhotoComment photoComment, final User accessor ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isCommentOfMenuCaller( photoComment, accessor );
	}
}
