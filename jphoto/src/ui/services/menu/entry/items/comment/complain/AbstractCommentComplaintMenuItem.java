package ui.services.menu.entry.items.comment.complain;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.AbstractEntryMenuItemComplaintCommand;
import ui.services.menu.entry.items.EntryMenuType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import ui.services.menu.entry.items.comment.ComplaintReasonType;

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
