package ui.services.menu.entry.items.comment.goTo;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

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
