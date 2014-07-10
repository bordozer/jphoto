package menuItems.photo.operation;

import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.photo.operation.PhotoMenuItemDelete;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.system.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoMenuItemDeleteTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void userCanNOTSeeMenuIfHeHasNoAccessToEditPhotoTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean hasAccessTo = false;

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemDelete( testData.getPhoto(), accessor, getServices( accessor, hasAccessTo ) ).hasAccessTo() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean hasAccessTo = true;

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemDelete( testData.getPhoto(), accessor, getServices( accessor, hasAccessTo ) ).hasAccessTo() );
	}

	@Test
	public void photoAuthorCommandTest() {

		final AbstractEntryMenuItemCommand command = new PhotoMenuItemDelete( testData.getPhoto(), testData.getAccessor(), getServices() ).getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( "PhotoMenuItem: Delete photo" ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "deletePhoto( %d );", testData.getPhoto().getId() ) );
	}

	private ServicesImpl getServices( final User accessor, final boolean hasAccessTo ) {
		final ServicesImpl services = getServices( accessor );

		services.setSecurityService( getSecurityService( accessor, hasAccessTo ) );

		return services;
	}

	private SecurityService getSecurityService( final User accessor, final boolean hasAccessTo ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userCanDeletePhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( hasAccessTo ).anyTimes();

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

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
