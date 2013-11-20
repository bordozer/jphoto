package core.general.menus.comment.items;

import core.enums.FavoriteEntryType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemBlackListRemove extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentMenuItemBlackListRemove";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.BLACK_LIST_REMOVE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		final User commentAuthor = getCommentAuthor( commentId );
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Remove $1 from your black list", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "jsonRPC.favoritesService.removeEntryFromFavoritesAjax( %d, %d, %d ); document.location.reload();", userWhoIsCallingMenu.getId(), commentAuthor.getId(), FavoriteEntryType.BLACKLIST.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		final User commentAuthor = photoComment.getCommentAuthor();
		return super.isAccessibleForComment( photoComment, userWhoIsCallingMenu ) && isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu ) && ! UserUtils.isUsersEqual( commentAuthor, userWhoIsCallingMenu ) && favoritesService.isUserInBlackListOfUser( userWhoIsCallingMenu.getId(), commentAuthor.getId() );
	}
}
