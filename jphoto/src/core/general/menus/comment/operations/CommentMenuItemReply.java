package core.general.menus.comment.operations;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
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

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {

				if ( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() ) {
					return TranslatorUtils.translate( "Reply to photo author ( anonymous )" );
				}

				return TranslatorUtils.translate( "Reply to $1$2", menuEntry.getCommentAuthor().getNameEscaped(), isCommentAuthorOwnerOfThePhoto() ? " ( photo's author )" : StringUtils.EMPTY );
			}

			@Override
			public String getMenuCommand() {
				if( isAccessorInTheBlackListOfCommentAuthor() ) {
					return String.format( "showInformationMessageNoAutoClose( '%s' )", TranslatorUtils.translate( "You are in the black list of $1. You can not reply.", menuEntry.getCommentAuthor().getNameEscaped() ) );
				}

				return String.format( "replyToComment( %d ); return false;", menuEntry.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return userCanCommentPhoto();
	}

	private boolean userCanCommentPhoto() {
		return services.getSecurityService().validateUserCanCommentPhoto( accessor, getPhoto() ).isValidationPassed();
	}
}
