package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemGoToCommentAuthorPhotos extends AbstractCommentMenuItem {

	public CommentMenuItemGoToCommentAuthorPhotos( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = getPhotoService().getPhotoQtyByUser( commentAuthor.getId() );
				return TranslatorUtils.translate( "$1: all photos ( $2 )", commentAuthor.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", commentAuthor.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photoComment, accessor ) ) {
			return false;
		}

		return super.isAccessibleFor( photoComment, accessor )
			   && getPhotoService().getPhotoQtyByUser( photoComment.getCommentAuthor().getId() ) > minPhotosForMenu( photoComment );
	}
}
