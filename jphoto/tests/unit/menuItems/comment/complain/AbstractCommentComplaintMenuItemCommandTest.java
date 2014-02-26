package menuItems.comment.complain;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.comment.complain.AbstractCommentComplaintMenuItem;
import core.general.menus.comment.complain.CommentMenuItemComplaintCustom;
import core.general.menus.comment.complain.CommentMenuItemComplaintSpam;
import core.general.menus.comment.complain.CommentMenuItemComplaintSwordWords;
import core.general.user.User;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractCommentComplaintMenuItemCommandTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void CommentMenuItemComplaintCustomTest() {
		final User accessor = testData.getAccessor();
		final Services services = new ServicesImpl();

		final CommentMenuItemComplaintCustom menuItem = new CommentMenuItemComplaintCustom( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "Custom complaint", ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT );
	}

	@Test
	public void CommentMenuItemComplaintSpamTest() {
		final User accessor = testData.getAccessor();
		final Services services = new ServicesImpl();

		final CommentMenuItemComplaintSpam menuItem = new CommentMenuItemComplaintSpam( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "Report spam", ComplaintReasonType.COMMENT_SPAM );
	}

	@Test
	public void CommentMenuItemComplaintSwordWordsTest() {
		final User accessor = testData.getAccessor();
		final Services services = new ServicesImpl();

		final CommentMenuItemComplaintSwordWords menuItem = new CommentMenuItemComplaintSwordWords( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "Report sword words or offence", ComplaintReasonType.COMMENT_SWORD_WORDS );
	}

	private void doAssert( final User accessor, final AbstractCommentComplaintMenuItem menuItem, final String expected, final ComplaintReasonType complaintReasonType ) {

		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );
		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, expected, command.getMenuText() );
		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS
			, command.getMenuCommand()
			, String.format( "%s( %d, %d, %d, %d ); return false;", AbstractEntryMenuItem.COMPLAINT_MESSAGE_JS_FUNCTION, EntryMenuType.COMMENT.getId(), testData.getComment().getId(), accessor.getId(), complaintReasonType.getId() ) );
	}
}
