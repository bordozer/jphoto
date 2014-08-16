package ui.services.menu.entry.items.comment.operations;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

import java.util.Date;

public class CommentMenuItemReply extends AbstractCommentMenuItem {

	private final Date time;

	public CommentMenuItemReply( final PhotoComment photoComment, final User accessor, final Services services, final Date time ) {
		super( photoComment, accessor, services );
		 this.time = time;
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_REPLY;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			final TranslatorService translatorService = getTranslatorService();

			@Override
			public String getMenuText() {

				if ( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() ) {
					return translatorService.translate( "CommentMenuItemReply: Reply to photo author ( anonymous )", getLanguage() );
				}

				return translatorService.translate( "CommentMenuItemReply: Reply to $1$2", getLanguage(), menuEntry.getCommentAuthor().getNameEscaped(), isCommentAuthorOwnerOfThePhoto() ? " ( photo's author )" : StringUtils.EMPTY );
			}

			@Override
			public String getMenuCommand() {
				if( isAccessorInTheBlackListOfCommentAuthor() ) {
					return String.format( "showUIMessage_InformationMessage_ManualClosing( '%s' )"
						, translatorService.translate( "CommentMenuItemReply: You are in the black list of $1. You can not reply.", getLanguage(), menuEntry.getCommentAuthor().getNameEscaped() ) );
				}

				return String.format( "replyToComment( %d );", menuEntry.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return userCanCommentPhoto();
	}

	private boolean userCanCommentPhoto() {
		return services.getSecurityService().validateUserCanCommentPhoto( accessor, getPhoto(), time, getLanguage() ).isValidationPassed();
	}
}
