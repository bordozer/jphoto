package core.general.menus.comment.complain;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public abstract class AbstractCommentComplaintMenuItem extends AbstractCommentMenuItem {

	protected abstract ComplaintReasonType getComplainReasonType();

	protected abstract String getMenuItemText();

	public AbstractCommentComplaintMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {
		if ( menuEntry.isCommentDeleted() ) {
			return false;
		}

		if ( ! isUserWhoIsCallingMenuLogged() ) {
			return false;
		}

		if ( isSuperAdminUser( accessor.getId() ) ) {
			return false;
		}

		if ( isCommentLeftByAccessor() ) {
			return false;
		}

		return true;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( getMenuItemText() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "%s( %d, %d, %d, %d ); return false;", COMPLAINT_MESSAGE_JS_FUNCTION, EntryMenuType.COMMENT.getId(), getId(), accessor.getId(), getComplainReasonType().getId() );
			}
		};
	}
}
