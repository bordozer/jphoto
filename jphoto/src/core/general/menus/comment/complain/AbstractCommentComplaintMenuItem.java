package core.general.menus.comment.complain;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.AbstractEntryMenuItemComplaintCommand;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;

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

		if ( ! isMenuAccessorLogged() ) {
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
		return new AbstractEntryMenuItemComplaintCommand<PhotoComment>( menuEntry, accessor, EntryMenuType.COMMENT, getComplainReasonType(), services ) {
			@Override
			public String getMenuText() {
				return getMenuItemText();
			}
		};
	}
}
