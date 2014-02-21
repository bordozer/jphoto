package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import utils.TranslatorUtils;

public class CommentAdminSubMenuItemLockUser extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentAdminSubMenuItemLockUser";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {

		final PhotoComment photoComment = photoCommentService.load( commentId );

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Lock user: $1", photoComment.getCommentAuthor().getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				final User commentAuthor = photoComment.getCommentAuthor();
				return String.format( "adminLockUser( %d, '%s' ); return false;", commentAuthor.getId(), commentAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return securityService.isSuperAdminUser( userWhoIsCallingMenu.getId() );
	}
}
