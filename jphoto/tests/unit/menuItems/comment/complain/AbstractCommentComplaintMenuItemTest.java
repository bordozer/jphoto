package menuItems.comment.complain;

import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.ComplaintReasonType;
import ui.services.menu.entry.items.comment.complain.AbstractCommentComplaintMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractCommentComplaintMenuItemTest extends AbstractCommentMenuItemTest_ {

	private static final String SOME_JS_FUNCTION = "someJSFunction();";
	private static final String MENU_TITLE = "Menu title";

	@Test
	public void notLoggedUserCanNotComplainTest() {
		final User user = NOT_LOGGED_USER;
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotComplainOnHisOwnCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void nobodyCanComplainOnDeletedCommentTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( comment, user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotComplainTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanComplainTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanComplainTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = testData.getAccessor(); // Does not matter
		final Services services = getServices( user );

		final AbstractEntryMenuItemCommand command = getMenuItemInstance( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), MENU_TITLE );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), SOME_JS_FUNCTION );
	}

	private AbstractCommentComplaintMenuItem getMenuItemInstance( final PhotoComment comment, final User user, final Services services ) {
		return new AbstractCommentComplaintMenuItem( comment, user, services ) {

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null; // "Does mot matter"
			}

			@Override
			protected ComplaintReasonType getComplainReasonType() {
				return null; // "Does mot matter"
			}

			@Override
			protected String getMenuItemText() {
				return null; // "Does mot matter"
			}

			@Override
			public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
				return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {
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
