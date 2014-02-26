package menuItems.photo.userOperation;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemDelete;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PhotoMenuItemDeleteTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean userCanEditPhoto = true;

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemDelete( testData.getPhoto(), accessor, getServicesEdit( accessor, userCanEditPhoto ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCommandTest() {

		final AbstractEntryMenuItemCommand command = new PhotoMenuItemDelete( testData.getPhoto(), null, new ServicesImpl() ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "Edit photo" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "editPhotoData( %d );", testData.getPhoto().getId() ) );
	}

	private ServicesImpl getServicesEdit( final User accessor, final boolean userCanEditPhoto ) {
		final ServicesImpl services = getServices( accessor );

		services.setSecurityService( getSecurityService( accessor, userCanEditPhoto ) );

		return services;
	}

	private SecurityService getSecurityService( final User accessor, final boolean userCanDeletePhoto ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userCanDeletePhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( userCanDeletePhoto ).anyTimes();

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getPhotoAuthor().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getPhotoAuthor() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1 ) ).andReturn( true ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2 ) ).andReturn( true ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
