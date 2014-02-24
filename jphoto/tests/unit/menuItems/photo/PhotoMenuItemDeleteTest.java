package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemDelete;
import core.general.user.User;
import core.services.security.ServicesImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemDeleteTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanNotSeeMenuIfHeDoesNotHaveAccessToDeletePhotoTest() {
		final User accessor = User.NOT_LOGGED_USER; // does not matter

		final ServicesImpl services = getServices( testData, accessor, false );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToDeletePhotoTest() {
		final User accessor = User.NOT_LOGGED_USER; // does not matter

		final ServicesImpl services = getServices( testData, accessor, true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCommandTest() {
		final User accessor = testData.getPhotoAuthor();

		final ServicesImpl services = getServices( testData, accessor, false );

		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuText(), "Delete your photo" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuCommand(), String.format( "deletePhoto( %d ); return false;", testData.getPhoto().getId() ) );
	}

	@Test
	public void adminCommandTest() {
		final User accessor = SUPER_MEGA_ADMIN;

		final ServicesImpl services = getServices( testData, accessor, false );

		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuText(), "Delete photo (ADMIN)" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuCommand(), String.format( "deletePhoto( %d ); return false;", testData.getPhoto().getId() ) );
	}
}
