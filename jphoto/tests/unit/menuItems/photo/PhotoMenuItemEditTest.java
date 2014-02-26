package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemEdit;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoMenuItemEditTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanNotSeeMenuIfHeDoesNotHaveAccessToEditPhotoTest() {
		final boolean userCanEditPhoto = false;

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemEdit( testData.getPhoto(), null, getServicesEdit( userCanEditPhoto ) ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {
		final boolean userCanEditPhoto = true;

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemEdit( testData.getPhoto(), null, getServicesEdit( userCanEditPhoto ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCommandTest() {
		final ServicesImpl services = new ServicesImpl();

		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), null, services ).getMenuItemCommand().getMenuText(), "Edit photo" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemEdit( testData.getPhoto(), null, services ).getMenuItemCommand().getMenuCommand(), String.format( "editPhotoData( %d );", testData.getPhoto().getId() ) );
	}

	private ServicesImpl getServicesEdit( final boolean userCanDeletePhoto ) {
		final ServicesImpl services = new ServicesImpl();

		services.setSecurityService( getSecurityService( userCanDeletePhoto ) );

		return services;
	}

	private SecurityService getSecurityService( final boolean userCanDeletePhoto ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userCanEditPhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( userCanDeletePhoto ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
