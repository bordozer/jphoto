package core.general.menus.comment.items;

import core.enums.FavoriteEntryType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemBlackListAdd extends AbstractCommentMenuItem {

	public CommentMenuItemBlackListAdd( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.BLACK_LIST_ADD;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final User commentAuthor = menuEntry.getCommentAuthor();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Add $1 to your black list", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "jsonRPC.favoritesService.addEntryToFavoritesAjax( %d, %d, %d ); document.location.reload();", accessor.getId(), commentAuthor.getId(), FavoriteEntryType.BLACKLIST.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		final User commentAuthor = photoComment.getCommentAuthor();
		return super.isAccessibleFor( photoComment, accessor )
			   && isUserWhoIsCallingMenuLogged( accessor )
			   && ! UserUtils.isUsersEqual( commentAuthor, accessor )
			   && ! getFavoritesService().isUserInBlackListOfUser( accessor.getId(), commentAuthor.getId() );
	}
}
