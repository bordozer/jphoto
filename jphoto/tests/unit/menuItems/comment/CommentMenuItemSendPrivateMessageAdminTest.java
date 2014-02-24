package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentMenuItemSendPrivateMessageAdmin;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemSendPrivateMessageAdminTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void adminCanSeeMenuTest() {
		final User accessor = SUPER_MEGA_ADMIN;

		final ServicesImpl services = getServices( testData, accessor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuTest() {
		final User accessor = SUPER_MEGA_ADMIN;

		final ServicesImpl services = getServices( testData, accessor );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessageAdmin( comment, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsHisCommentTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final ServicesImpl services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuTest() {
		final User accessor = testData.getAccessor();

		final ServicesImpl services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeMenuTest() {
		final User accessor = testData.getCommentAuthor();

		final ServicesImpl services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeMenuTest() {
		final User accessor = testData.getPhotoAuthor();

		final ServicesImpl services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, accessor );

		final AbstractEntryMenuItemCommand command = new CommentMenuItemSendPrivateMessageAdmin( testData.getComment(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "Send admin message to %s", testData.getCommentAuthor().getNameEscaped() ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), testData.getCommentAuthor().getId(), testData.getCommentAuthor().getNameEscaped() ) );
	}
}
