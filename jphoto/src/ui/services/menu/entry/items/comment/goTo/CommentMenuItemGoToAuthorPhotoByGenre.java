package ui.services.menu.entry.items.comment.goTo;

import core.general.genre.Genre;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;

public class CommentMenuItemGoToAuthorPhotoByGenre extends AbstractCommentGoToAuthorPhotos {

	public CommentMenuItemGoToAuthorPhotoByGenre( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {

		final User commentAuthor = menuEntry.getCommentAuthor();
		final Genre genre = getGenre( menuEntry );

		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: $1: photos in category '$2' ( $3 )", getLanguage(), commentAuthor.getNameEscaped(), getGenreNameTranslated( genre ), String.valueOf( getPhotoQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByGenre( %d, %d );", commentAuthor.getId(), genre.getId() );
			}
		};
	}

	@Override
	public int getPhotoQty() {
		final Genre genre = getGenre( menuEntry );
		return getPhotoService().getPhotosCountByUserAndGenre( menuEntry.getCommentAuthor().getId(), genre.getId() );
	}
}
