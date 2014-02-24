package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemSendPrivateMessage extends AbstractCommentMenuItem {

	public CommentMenuItemSendPrivateMessage( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			private User commentAuthor = menuEntry.getCommentAuthor();

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), commentAuthor.getId(), commentAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( !isUserWhoIsCallingMenuLogged() ) {
			return false;
		}

		if ( isCommentLeftByAccessor() ) {
			return false;
		}

		final boolean isAccessorInBlackListOfCommentAuthor = isAccessorInTheBlackListOfCommentAuthor();

		final boolean photoAuthorAlwaysCanSeeMenuButHeIsInTheBlackListOfCommentAuthor = isAccessorOwnerOfTheThePhoto() && !isAccessorInBlackListOfCommentAuthor;
		if ( photoAuthorAlwaysCanSeeMenuButHeIsInTheBlackListOfCommentAuthor ) {
			return true;
		}

		if( isAccessorInBlackListOfCommentAuthor ) {
			return false;
		}

		return !isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod();
	}

}
