package menuItems.photo.admin;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.admin.PhotoAdminSubMenuItemLockPhotoAuthor;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoAdminSubMenuItemLockUserTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockPhotoAuthorMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;
		doAssertFalse( accessor, getServicesForTest( accessor ) );
	}

	@Test
	public void usualUserCanNotSeeLockPhotoAuthorMenuTest() {
		final User accessor = testData.getAccessor();
		doAssertFalse( accessor, getServicesForTest( accessor ) );
	}

	@Test
	public void photoAuthorCanNotSeeLockPhotoAuthorMenuTest() {
		final User accessor = testData.getPhotoAuthor();
		doAssertFalse( accessor, getServicesForTest( accessor ) );
	}

	@Test
	public void adminCanNotSeeLockPhotoAuthorMenuIfTheAuthorIsAnotherAdminTest() {
		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_2.getId() );

		final User accessor = SUPER_ADMIN_1;
		doAssertFalse( accessor, getServicesForTest( accessor ) );
	}

	@Test
	public void adminCanSeeLockPhotoAuthorMenuTest() {
		final User accessor = SUPER_ADMIN_1;
		doAssertTrue( accessor, getServicesForTest( accessor ) );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;

		final AbstractEntryMenuItemCommand command = new PhotoAdminSubMenuItemLockPhotoAuthor( testData.getPhoto(), accessor, getServicesForTest( accessor ) ).getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), String.format( "Lock photo author: %s", photoAuthor.getNameEscaped() ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "adminLockUser( %d, '%s' ); return false;", photoAuthor.getId(), photoAuthor.getNameEscaped() ) );
	}

	@Test
	public void cssClassTest() {
		final User accessor = User.NOT_LOGGED_USER; // does not matter
		final PhotoAdminSubMenuItemLockPhotoAuthor menuItem = new PhotoAdminSubMenuItemLockPhotoAuthor( testData.getPhoto(), accessor, getServicesForTest( accessor ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_ADMIN );
	}

	private ServicesImpl getServicesForTest( final User accessor ) {
		return getServices( accessor );
	}

	private void doAssertTrue( final User accessor, final Services services ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoAdminSubMenuItemLockPhotoAuthor( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	private void doAssertFalse( final User accessor, final Services services ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoAdminSubMenuItemLockPhotoAuthor( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}
}
