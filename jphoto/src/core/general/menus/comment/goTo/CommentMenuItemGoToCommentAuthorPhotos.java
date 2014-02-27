package core.general.menus.comment.goTo;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemGoToCommentAuthorPhotos extends AbstractCommentGoToAuthorPhotos {

	public CommentMenuItemGoToCommentAuthorPhotos( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, getEntryMenuType() ) {

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
	public int getPhotoQty() {
		return getPhotoService().getPhotoQtyByUser( menuEntry.getCommentAuthor().getId() );
	}
}
