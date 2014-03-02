package core.general.menus;

import core.general.menus.comment.ComplaintReasonType;
import core.general.user.User;

public abstract class AbstractEntryMenuItemComplaintCommand<T extends PopupMenuAssignable> extends AbstractEntryMenuItemCommand<T> {

	public static final String COMPLAINT_MESSAGE_JS_FUNCTION = "sendComplaintMessage";

	private User accessor;
	private final ComplaintReasonType complaintReasonType;

	public AbstractEntryMenuItemComplaintCommand( final T menuEntry, final User accessor, final ComplaintReasonType complaintReasonType ) {
		super( menuEntry );
		this.accessor = accessor;
		this.complaintReasonType = complaintReasonType;
	}

	@Override
	public String getMenuCommand() {
		return String.format( "%s( %d, %d, %d, %d ); return false;"
			, COMPLAINT_MESSAGE_JS_FUNCTION
			, EntryMenuType.COMMENT.getId()
			, getId()
			, accessor.getId()
			, complaintReasonType.getId()
		);
	}
}
