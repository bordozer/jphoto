package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByAlbum;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import core.services.user.UserPhotoAlbumService;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemGoToAuthorPhotoByAlbumTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosInAlbumTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final int albumPhotosQty = 2;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoInAlbumTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final int albumPhotosQty = 1;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void notLoggedCanSeeMenuIfThereIsMoreThenOnePhotosInTheAlbumTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final int albumPhotosQty = 2;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosInTheAlbumTest() {
		final User accessor = SUPER_ADMIN_1;
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosInTheAlbumTest() {
		final User accessor = SUPER_ADMIN_1;
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {
		final User accessor = testData.getPhotoAuthor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosInTheAlbumTest() {
		final User accessor = testData.getPhotoAuthor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final int albumPhotosQty = 1;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final User accessor = testData.getPhotoAuthor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = true;
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosInTheAlbumTest() {
		final User accessor = testData.getAccessor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int albumPhotosQty = 1;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosInTheAlbumTest() {
		final User accessor = testData.getAccessor();
		final UserPhotoAlbum photoAlbum = getUserPhotoAlbum();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int albumPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( photoAlbum.getId(), albumPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), accessor, services, photoAlbum ).isAccessibleFor() );
	}

	private ConfigurationService getConfigurationService( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private SecurityService getSecurityService( final User accessor, final boolean isPhotoAuthorNameMustBeHidden ) {

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.getPhoto(), accessor ) ).andReturn( isPhotoAuthorNameMustBeHidden ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private UserPhotoAlbumService getUserPhotoAlbumService( final int photoAlbumId, final int albumPhotosQty ) {
		final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock( UserPhotoAlbumService.class );

		EasyMock.expect( userPhotoAlbumService.getUserPhotoAlbumPhotosQty( photoAlbumId ) ).andReturn( albumPhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userPhotoAlbumService );

		return userPhotoAlbumService;
	}

	private UserPhotoAlbum getUserPhotoAlbum() {
		final UserPhotoAlbum album = new UserPhotoAlbum();

		album.setId( 881 );
		album.setName( "Photo album" );

		return album;
	}
}
