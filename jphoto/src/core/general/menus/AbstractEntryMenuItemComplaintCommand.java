package core.general.menus;

import core.general.menus.comment.ComplaintReasonType;
import core.general.user.User;
import core.services.system.Services;

public abstract class AbstractEntryMenuItemComplaintCommand<T extends PopupMenuAssignable> extends AbstractEntryMenuItemCommand<T> {

	public static final String COMPLAINT_MESSAGE_JS_FUNCTION = "sendComplaintMessage";

	private final EntryMenuType entryMenuType;
	private final ComplaintReasonType complaintReasonType;

	public AbstractEntryMenuItemComplaintCommand( final T menuEntry, final User accessor, final EntryMenuType entryMenuType, final ComplaintReasonType complaintReasonType, final Services services ) {
		super( menuEntry, accessor, services );
		this.entryMenuType = entryMenuType;
		this.complaintReasonType = complaintReasonType;
	}

	@Override
	public String getMenuCommand() {
		return String.format( "%s( %d, %d, %d, %d ); return false;"
			, COMPLAINT_MESSAGE_JS_FUNCTION
			, entryMenuType.getId()
			, getId()
			, accessor.getId()
			, complaintReasonType.getId()
		);
	}
}
