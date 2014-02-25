package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotos;
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

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final int genrePhotosQty = 2;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoInGenreTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final int genrePhotosQty = 1;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = SUPER_ADMIN_1;
		final int genrePhotosQty = 2;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosInGenreTest() {
		final User accessor = SUPER_ADMIN_1;
		final int genrePhotosQty = 2;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {
		final User accessor = testData.getPhotoAuthor();
		final int genrePhotosQty = 2;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosInGenreTest() {
		final User accessor = testData.getPhotoAuthor();
		final int genrePhotosQty = 1;
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = true; // sick!
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosInGenreTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int genrePhotosQty = 1;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosInGenreTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int genrePhotosQty = 2;

		final ServicesImpl services = getServices( accessor, genrePhotosQty, showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, isPhotoAuthorNameMustBeHidden );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	private ServicesImpl getServices( final User accessor, final int genrePhotosQty, final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn, final boolean photoAuthorNameMustBeHidden ) {

		final ServicesImpl services = getServices( testData, accessor );

		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, photoAuthorNameMustBeHidden ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( genrePhotosQty ) );

		return services;
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

		EasyMock.expect( photoService.getPhotoQtyByUser( testData.getPhotoAuthor().getId() ) ).andReturn( genrePhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
