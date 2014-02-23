package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.items.AbstractCommentComplaintMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractCommentComplaintMenuItemTest extends AbstractCommentMenuItemTest_ {

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
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuItemInstance( testData.getComment(), user, services ).isAccessibleFor() );
	}

	private AbstractCommentComplaintMenuItem getMenuItemInstance( final PhotoComment comment, final User user, final Services services ) {
		return new AbstractCommentComplaintMenuItem( comment, user, services ) {

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null; // "Does mot matter"
			}

			@Override
			public AbstractEntryMenuItemCommand getMenuItemCommand() {
				return null; //"Does mot matter"
			}
		};
	}
}
