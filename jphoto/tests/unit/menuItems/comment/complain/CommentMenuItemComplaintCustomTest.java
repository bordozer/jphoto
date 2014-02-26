package menuItems.comment.complain;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.comment.complain.CommentMenuItemComplaintCustom;
import core.general.user.User;
import core.services.security.Services;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentMenuItemComplaintCustomTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotComplainTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, accessor );

		final CommentMenuItemComplaintCustom menuItem = new CommentMenuItemComplaintCustom( testData.getComment(), accessor, services );
		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );

		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();
		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, command.getMenuText(), "Custom complaint" );
		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS
			, command.getMenuCommand()
			, String.format( "%s( %d, %d, %d, %d ); return false;"
					, AbstractEntryMenuItem.COMPLAINT_MESSAGE_JS_FUNCTION
					, EntryMenuType.COMMENT.getId()
					, testData.getComment().getId()
					, accessor.getId()
					, ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT.getId()
				) );
	}
}
