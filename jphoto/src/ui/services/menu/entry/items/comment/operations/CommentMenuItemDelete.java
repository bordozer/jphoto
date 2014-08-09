package ui.services.menu.entry.items.comment.operations;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import utils.UserUtils;

public class CommentMenuItemDelete extends AbstractCommentMenuItem {

	public CommentMenuItemDelete( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				if ( isCommentLeftByAccessor() ) {
					return getTranslatorService().translate( "CommentMenuItemDelete: Delete comment", getLanguage() );
				}

				return getTranslatorService().translate( "CommentMenuItemDelete: Delete comment (as photo author)", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d );", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( menuEntry.isCommentDeleted() ) {
			return false;
		}

		if ( ! UserUtils.isLoggedUser( accessor ) ) {
			return false;
		}

		if ( UserUtils.isUserOwnThePhoto( accessor, getPhoto() ) ) {
			return true;
		}

		// TODO: should be allowed deletion of admin messages?

		return isCommentLeftByAccessor();
	}
}
