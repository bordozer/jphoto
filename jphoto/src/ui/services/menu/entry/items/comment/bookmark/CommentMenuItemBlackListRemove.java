package ui.services.menu.entry.items.comment.bookmark;

import core.enums.FavoriteEntryType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import utils.UserUtils;

public class CommentMenuItemBlackListRemove extends AbstractCommentMenuItem {

	public CommentMenuItemBlackListRemove( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.BLACK_LIST_REMOVE;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Remove $1 from your black list", getLanguage(), commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "jsonRPC.ajaxService.removeEntryFromFavoritesAjax( %d, %d, %d ); document.location.reload();", accessor.getId(), commentAuthor.getId(), FavoriteEntryType.BLACKLIST.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		return super.isAccessibleFor()
			   && isMenuAccessorLogged()
			   && ! UserUtils.isUsersEqual( commentAuthor, accessor )
			   && getFavoritesService().isUserInBlackListOfUser( accessor.getId(), commentAuthor.getId() );
	}
}
