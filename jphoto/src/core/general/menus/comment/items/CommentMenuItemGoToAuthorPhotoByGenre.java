package core.general.menus.comment.items;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemGoToAuthorPhotoByGenre extends AbstractCommentMenuItem {

	public CommentMenuItemGoToAuthorPhotoByGenre( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		final Genre genre = getGenre( menuEntry );

		final User commentAuthor = menuEntry.getCommentAuthor();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUserAndGenre = getPhotoService().getPhotoQtyByUserAndGenre( commentAuthor.getId(), genre.getId() );
				return TranslatorUtils.translate( "$1: photos in category '$2' ( $3 )", commentAuthor.getNameEscaped(), genre.getName(), String.valueOf( photoQtyByUserAndGenre ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByGenre( %d, %d );", commentAuthor.getId(), genre.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photoComment, accessor ) ) {
			return false;
		}

		final Genre genre = getGenre( photoComment );
		return super.isAccessibleFor( photoComment, accessor )
			   && getPhotoService().getPhotoQtyByUserAndGenre( photoComment.getCommentAuthor().getId(), genre.getId() ) > minPhotosForMenu( photoComment );
	}

	protected Genre getGenre( final PhotoComment photoComment ) {
		final int photoId = photoComment.getPhotoId();
		final Photo photo = getPhotoService().load( photoId );

		return services.getGenreService().load( photo.getGenreId() );
	}
}
