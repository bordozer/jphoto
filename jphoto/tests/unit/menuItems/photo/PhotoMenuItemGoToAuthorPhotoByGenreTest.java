package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final int genrePhotosQty = 2;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoInGenreTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final int genrePhotosQty = 1;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = SUPER_ADMIN_1;
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosInGenreTest() {
		final User accessor = SUPER_ADMIN_1;
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false; // sick!
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosInGenreTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true; // sick!
		final int genrePhotosQty = 1; // sick!

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final boolean isPhotoAuthorNameMustBeHidden = false; // sick!
		final int genrePhotosQty = 2; // sick!

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = true; // sick!
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosInGenreTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int genrePhotosQty = 1;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), accessor, services ).isAccessibleFor() );
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

	private GenreService getGenreService() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( testData.getPhoto().getGenreId() ) ).andReturn( testData.getGenre() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private PhotoService getPhotoService( final int genrePhotosQty ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( testData.getPhotoAuthor().getId(), testData.getPhoto().getGenreId() ) ).andReturn( genrePhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
