package core.general.menus.comment.operations;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;

public class CommentMenuItemReply extends AbstractCommentMenuItem {

	public CommentMenuItemReply( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
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
					return translatorService.translate( "Reply to photo author ( anonymous )", getLanguage() );
				}

				return translatorService.translate( "Reply to $1$2", getLanguage(), menuEntry.getCommentAuthor().getNameEscaped(), isCommentAuthorOwnerOfThePhoto() ? " ( photo's author )" : StringUtils.EMPTY );
			}

			@Override
			public String getMenuCommand() {
				if( isAccessorInTheBlackListOfCommentAuthor() ) {
					return String.format( "showInformationMessageNoAutoClose( '%s' )"
						, translatorService.translate( "You are in the black list of $1. You can not reply.", getLanguage(), menuEntry.getCommentAuthor().getNameEscaped() ) );
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
		return services.getSecurityService().validateUserCanCommentPhoto( accessor, getPhoto(), getLanguage() ).isValidationPassed();
	}
}
