package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;
import utils.TranslatorUtils;

public class CommentMenuItemReply extends AbstractCommentMenuItem {

	public CommentMenuItemReply( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_REPLY;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final int commentId = menuEntry.getId();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {

				if ( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( menuEntry, accessor ) ) {
					return TranslatorUtils.translate( "Reply to photo author ( anonymous )" );
				}

				return TranslatorUtils.translate( "Reply to $1$2", getCommentAuthor( commentId ).getNameEscaped(), isCommentAuthorOwnerOfPhoto( menuEntry ) ? " ( photo's author )" : StringUtils.EMPTY );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "replyToComment( %d ); return false;", commentId );
			}
		};
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		return isUserWhoIsCallingMenuLogged( accessor ) && !isCommentLeftByUserWhoIsCallingMenu( photoComment, accessor );
	}
}
