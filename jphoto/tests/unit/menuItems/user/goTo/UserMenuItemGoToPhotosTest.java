package menuItems.user.goTo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.goTo.UserMenuItemGoToPhotos;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import menuItems.user.AbstractUserMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserMenuItemGoToPhotosTest extends AbstractUserMenuItemTest_ {

	@Test
	public void usualUserCanNotSeeGoToPhotosMenuIfUserHasNoPhotosTest() {

		final Parameters parameters = new Parameters( 0, true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemGoToPhotos( testData.getUser(), testData.getAccessor(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeGoToPhotosMenuIfUserHasLessThenTwoPhotosTest() {

		final Parameters parameters = new Parameters( 1, true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemGoToPhotos( testData.getUser(), testData.getAccessor(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeOwnGoToPhotosMenuIfItIsSwitchedOffTest() {

		final Parameters parameters = new Parameters( 7, false );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemGoToPhotos( testData.getUser(), testData.getUser(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanSeeGoToPhotosMenuIfUserHasAtLeastTwoPhotoTest() {

		final Parameters parameters = new Parameters( 2, true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( testData.getUser(), testData.getAccessor(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanSeeGoToPhotosMenuOfAdminTest() {

		final Parameters parameters = new Parameters( 2, true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( SUPER_ADMIN_1, testData.getAccessor(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeOwnGoToPhotosMenuIfItIsSwitchedOnTest() {

		final Parameters parameters = new Parameters( 7, true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( testData.getUser(), testData.getUser(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeGoToPhotosMenuOfAnotherUserIfItIsSwitchedOffTest() {

		final Parameters parameters = new Parameters( 7, false );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemGoToPhotos( testData.getUser(), testData.getAccessor(), getServicesGoTo( parameters ) ).isAccessibleFor() );
	}

	private ServicesImpl getServicesGoTo( final Parameters parameters ) {
		final ServicesImpl services = getServices();

		services.setConfigurationService( getConfigurationService( parameters.isShowOwnGoToPhotoConfiguration() ) );
		services.setPhotoService( getPhotoService( parameters.getPhotosQty(), testData.getUser() ) );

		return services;
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final int photosQty = 4;
		final boolean showOwnGoToPhotoConfiguration = true;

		final ServicesImpl services = getServicesGoTo( new Parameters( photosQty, showOwnGoToPhotoConfiguration ) );

		final AbstractEntryMenuItemCommand command = new UserMenuItemGoToPhotos( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "%s: all photos ( %d )", testData.getUser().getNameEscaped(), photosQty ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotos( %d );", testData.getUser().getId() ) );
	}

	private ConfigurationService getConfigurationService( final boolean showOwnGoToPhotoConfiguration ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showOwnGoToPhotoConfiguration ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private PhotoService getPhotoService( final int photosQty, final User user ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUser( user.getId() ) ).andReturn( photosQty ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUser( SUPER_ADMIN_1.getId() ) ).andReturn( photosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private static class Parameters {

		private final int photosQty;
		private final boolean showOwnGoToPhotoConfiguration;

		private Parameters( final int photosQty, final boolean showOwnGoToPhotoConfiguration ) {
			this.photosQty = photosQty;
			this.showOwnGoToPhotoConfiguration = showOwnGoToPhotoConfiguration;
		}

		public int getPhotosQty() {
			return photosQty;
		}

		public boolean isShowOwnGoToPhotoConfiguration() {
			return showOwnGoToPhotoConfiguration;
		}
	}
}
