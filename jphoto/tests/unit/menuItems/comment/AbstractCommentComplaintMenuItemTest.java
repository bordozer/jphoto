package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.items.AbstractCommentComplaintMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractCommentComplaintMenuItemTest extends AbstractCommentMenuItemTest_ {

	private static final String SOME_JS_FUNCTION = "someJSFunction();";
	private static final String MENU_TITLE = "Menu title";

	@Test
	public void notLoggedUserCanNotComplainTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotComplainOnHisOwnCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void nobodyCanComplainOnDeletedCommentTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( comment, user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotComplainTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanComplainTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanComplainTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = testData.getAccessor(); // Does not matter
		final Services services = getServices( testData, user );

		final AbstractEntryMenuItemCommand command = getMenuItemInstance( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), MENU_TITLE );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), SOME_JS_FUNCTION );
	}

	private AbstractCommentComplaintMenuItem getMenuItemInstance( final PhotoComment comment, final User user, final Services services ) {
		return new AbstractCommentComplaintMenuItem( comment, user, services ) {

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null; // "Does mot matter"
			}

			@Override
			public AbstractEntryMenuItemCommand getMenuItemCommand() {
				return new AbstractEntryMenuItemCommand( EntryMenuOperationType.COMMENT_COMPLAINT_CUSTOM ) {
					@Override
					public String getMenuText() {
						return MENU_TITLE;
					}

					@Override
					public String getMenuCommand() {
						return SOME_JS_FUNCTION;
					}
				};
			}
		};
	}
}
