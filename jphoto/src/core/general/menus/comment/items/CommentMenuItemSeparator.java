package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import org.apache.commons.lang.StringUtils;

public class CommentMenuItemSeparator extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "separatorMenuItem";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEPARATOR;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return StringUtils.EMPTY;
			}

			@Override
			public String getMenuCommand() {
				return StringUtils.EMPTY;
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return true;
	}
}
