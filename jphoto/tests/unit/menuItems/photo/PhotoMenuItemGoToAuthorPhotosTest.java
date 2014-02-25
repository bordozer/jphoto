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

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractGoToAuthorPhotosTest_ {

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 2 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 1 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 1 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );
		goToParameters.setPhotoAuthorNameMustBeHidden( true );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 1 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );

		final ServicesImpl services = getServicesGoTo( goToParameters );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), services ).isAccessibleFor() );
	}

	protected ServicesImpl getServicesGoTo( final GoToParameters goToParameters ) {

		final ServicesImpl services = getServices( testData, goToParameters.getAccessor() );

		services.setConfigurationService( getConfigurationService( goToParameters.isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() ) );
		services.setSecurityService( getSecurityService( goToParameters.getAccessor(), goToParameters.isPhotoAuthorNameMustBeHidden() ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( goToParameters.getGenrePhotosQty() ) );

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
