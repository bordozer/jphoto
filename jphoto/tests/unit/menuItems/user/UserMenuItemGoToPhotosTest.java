package menuItems.user;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.items.UserMenuItemGoToPhotos;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserMenuItemGoToPhotosTest extends AbstractUserMenuItemTest_ {

	@Test
	public void usualUserCanSeeGoToPhotosMenuIfUserHasAtLeastOnePhotoTest() {
		final User accessor = testData.getAccessor();

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( true ) );
		services.setPhotoService( getPhotoService( testData.getUser(), 1 ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeGoToPhotosMenuIfUserHasNoPhotosTest() {
		final User accessor = testData.getAccessor();

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( true ) );
		services.setPhotoService( getPhotoService( testData.getUser(), 0 ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeOwnGoToPhotosMenuIfItIsSwitchedOffTest() {
		final User accessor = testData.getUser();

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( false ) );
		services.setPhotoService( getPhotoService( testData.getUser(), 7 ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeOwnGoToPhotosMenuIfItIsSwitchedOnTest() {
		final User accessor = testData.getUser();

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( true ) );
		services.setPhotoService( getPhotoService( testData.getUser(), 7 ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final int qty = 4;

		final ServicesImpl services = getServices( testData, accessor );
		services.setPhotoService( getPhotoService( testData.getUser(), qty ) );

		final AbstractEntryMenuItemCommand command = new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "%s: all photos ( %d )", testData.getUser().getNameEscaped(), qty ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotos( %d );", testData.getUser().getId() ) );
	}

	private ConfigurationService getConfigurationService( final boolean showOwnMenuEntries ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showOwnMenuEntries ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private PhotoService getPhotoService( final User user, final int qty ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUser( user.getId() ) ).andReturn( qty ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
