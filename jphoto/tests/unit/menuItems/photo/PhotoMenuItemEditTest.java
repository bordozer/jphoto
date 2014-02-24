package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemEdit;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemEditTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanNotSeeMenuIfHeDoesNotHaveAccessToDeletePhotoTest() {
		final User accessor = User.NOT_LOGGED_USER; // does not matter

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, false ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToDeletePhotoTest() {
		final User accessor = User.NOT_LOGGED_USER; // does not matter

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, true ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCommandTest() {
		final User accessor = testData.getPhotoAuthor();

		final ServicesImpl services = getServices( testData, accessor );

		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuText(), "Edit your photo" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuCommand(), String.format( "editPhotoData( %d );", testData.getPhoto().getId() ) );
	}

	@Test
	public void adminCommandTest() {
		final User accessor = SUPER_ADMIN_1;

		final ServicesImpl services = getServices( testData, accessor );

		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuText(), "Edit photo (ADMIN)" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), accessor, services ).getMenuItemCommand().getMenuCommand(), String.format( "editPhotoData( %d );", testData.getPhoto().getId() ) );
	}

	private SecurityService getSecurityService( final User accessor, final boolean userCanDeletePhoto ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhotoAuthor().getId() == accessor.getId() ).anyTimes();
		EasyMock.expect( securityService.userCanEditPhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( userCanDeletePhoto ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
