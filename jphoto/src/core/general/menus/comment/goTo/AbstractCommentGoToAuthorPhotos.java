package core.general.menus.comment.goTo;

import core.general.genre.Genre;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;

public abstract class AbstractCommentGoToAuthorPhotos extends AbstractCommentMenuItem {

	public AbstractCommentGoToAuthorPhotos( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	public abstract int getPhotoQty();

	@Override
	public boolean isAccessibleFor() {
		if ( hideMenuItemBecauseEntryOfMenuCaller() ) {
			return false;
		}

		if ( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() ) {
			return false;
		}

		return getPhotoQty() > minPhotosForMenu();
	}
}
