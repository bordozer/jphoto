package menuItems.comment.complain;

import ui.services.menu.entry.items.AbstractEntryMenuItem;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.AbstractEntryMenuItemComplaintCommand;
import ui.services.menu.entry.items.EntryMenuType;
import ui.services.menu.entry.items.comment.ComplaintReasonType;
import ui.services.menu.entry.items.comment.complain.AbstractCommentComplaintMenuItem;
import ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintCustom;
import ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintSpam;
import ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintSwordWords;
import core.general.user.User;
import core.services.system.Services;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractCommentComplaintMenuItemCommandTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void CommentMenuItemComplaintCustomTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		final CommentMenuItemComplaintCustom menuItem = new CommentMenuItemComplaintCustom( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "CommentMenuItemComplaint: Custom complaint", ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT );
	}

	@Test
	public void CommentMenuItemComplaintSpamTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		final CommentMenuItemComplaintSpam menuItem = new CommentMenuItemComplaintSpam( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "CommentMenuItemComplaint: Spam", ComplaintReasonType.COMMENT_SPAM );
	}

	@Test
	public void CommentMenuItemComplaintSwordWordsTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		final CommentMenuItemComplaintSwordWords menuItem = new CommentMenuItemComplaintSwordWords( testData.getComment(), accessor, services );

		doAssert( accessor, menuItem, "CommentMenuItemComplaint: Report sword words or offence", ComplaintReasonType.COMMENT_SWORD_WORDS );
	}

	private void doAssert( final User accessor, final AbstractCommentComplaintMenuItem menuItem, final String expected, final ComplaintReasonType complaintReasonType ) {

		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS );

		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, translated( expected ), command.getMenuText() );

		assertEquals( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS
				, command.getMenuCommand()
				, String.format( "%s( %d, %d, %d, %d );"
				, AbstractEntryMenuItemComplaintCommand.COMPLAINT_MESSAGE_JS_FUNCTION
				, EntryMenuType.COMMENT.getId()
				, testData.getComment().getId()
				, accessor.getId()
				, complaintReasonType.getId()
			)
		);
	}
}
