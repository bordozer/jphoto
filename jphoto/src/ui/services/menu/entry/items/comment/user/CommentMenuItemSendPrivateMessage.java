package ui.services.menu.entry.items.comment.user;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

public class CommentMenuItemSendPrivateMessage extends AbstractCommentMenuItem {

	public CommentMenuItemSendPrivateMessage( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			private User commentAuthor = menuEntry.getCommentAuthor();

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Send private message to $1", getLanguage(), commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), commentAuthor.getId(), commentAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( !isMenuAccessorLogged() ) {
			return false;
		}

		if ( isCommentLeftByAccessor() ) {
			return false;
		}

		if ( isSuperAdminUser( accessor ) ) {
			return true;
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
