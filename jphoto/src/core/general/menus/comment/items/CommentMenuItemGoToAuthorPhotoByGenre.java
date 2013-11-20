package core.general.menus.comment.items;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;

public class CommentMenuItemGoToAuthorPhotoByGenre extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentMenuItemGoToAuthorPhotoByGenre";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		final PhotoComment photoComment = photoCommentService.load( commentId );
		final Genre genre = getGenre( photoComment );

		final User commentAuthor = getCommentAuthor( commentId );

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUserAndGenre = photoService.getPhotoQtyByUserAndGenre( commentAuthor.getId(), genre.getId() );
				return TranslatorUtils.translate( "$1: photos in category '$2' ( $3 )", commentAuthor.getNameEscaped(), genre.getName(), String.valueOf( photoQtyByUserAndGenre ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByGenre( %d, %d );", commentAuthor.getId(), genre.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photoComment, userWhoIsCallingMenu ) ) {
			return false;
		}

		final Genre genre = getGenre( photoComment );
		return super.isAccessibleForComment( photoComment, userWhoIsCallingMenu )
			   && photoService.getPhotoQtyByUserAndGenre( photoComment.getCommentAuthor().getId(), genre.getId() ) > minPhotosForMenu( photoComment );
	}

	protected Genre getGenre( final PhotoComment photoComment ) {
		final int photoId = photoComment.getPhotoId();
		final Photo photo = photoService.load( photoId );

		return genreService.load( photo.getGenreId() );
	}
}
