package core.general.menus.comment.bookmark;

import core.enums.FavoriteEntryType;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;
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
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Remove $1 from your black list", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "jsonRPC.favoritesService.removeEntryFromFavoritesAjax( %d, %d, %d ); document.location.reload();", accessor.getId(), commentAuthor.getId(), FavoriteEntryType.BLACKLIST.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		final User commentAuthor = menuEntry.getCommentAuthor();
		return super.isAccessibleFor()
			   && isUserWhoIsCallingMenuLogged()
			   && ! UserUtils.isUsersEqual( commentAuthor, accessor )
			   && getFavoritesService().isUserInBlackListOfUser( accessor.getId(), commentAuthor.getId() );
	}
}
