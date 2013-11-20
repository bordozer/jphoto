package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;

public class CommentMenuItemGoToCommentAuthorPhotos extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentMenuItemGoToCommentAuthorPhotos";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		final User commentAuthor = getCommentAuthor( commentId );
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = photoService.getPhotoQtyByUser( commentAuthor.getId() );
				return TranslatorUtils.translate( "$1: all photos ( $2 )", commentAuthor.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", commentAuthor.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photoComment, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleForComment( photoComment, userWhoIsCallingMenu )
			   && photoService.getPhotoQtyByUser( photoComment.getCommentAuthor().getId() ) > minPhotosForMenu( photoComment );
	}
}
