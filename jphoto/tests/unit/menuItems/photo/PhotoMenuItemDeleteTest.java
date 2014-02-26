package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemDelete;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoMenuItemDeleteTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanNotSeeMenuIfHeDoesNotHaveAccessToDeletePhotoTest() {
		final boolean userCanDeletePhoto = false;

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemDelete( testData.getPhoto(), null, getServicesDelete( userCanDeletePhoto ) ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToDeletePhotoTest() {
		final boolean userCanDeletePhoto = true;

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemDelete( testData.getPhoto(), null, getServicesDelete( userCanDeletePhoto ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCommandTest() {
		final ServicesImpl servicesDelete = new ServicesImpl();

		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), null, servicesDelete ).getMenuItemCommand().getMenuText(), "Delete photo" );
		assertEquals( WRONG_COMMAND, new PhotoMenuItemDelete( testData.getPhoto(), null, servicesDelete ).getMenuItemCommand().getMenuCommand(), String.format( "deletePhoto( %d ); return false;", testData.getPhoto().getId() ) );
	}

	private ServicesImpl getServicesDelete( final boolean userCanDeletePhoto ) {
		final ServicesImpl services = new ServicesImpl();

		services.setSecurityService( getSecurityService( userCanDeletePhoto ) );

		return services;
	}

	private SecurityService getSecurityService( final boolean userCanDeletePhoto ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userCanDeletePhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( userCanDeletePhoto ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
