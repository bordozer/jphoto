package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotos;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractGoToAuthorPhotosTest_ {

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 1 );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 1 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );
		goToParameters.setPhotoAuthorNameMustBeHidden( true );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 1 );

		assertF( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ).isAccessibleFor() );
	}

	protected ServicesImpl getServicesGoTo( final GoToParameters goToParameters ) {

		final ServicesImpl services = getServices( testData, goToParameters.getAccessor() );

		services.setConfigurationService( getConfigurationServiceGoTo( goToParameters.isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() ) );
		services.setSecurityService( getSecurityServiceGoTo( goToParameters.getAccessor(), goToParameters.isPhotoAuthorNameMustBeHidden() ) );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( goToParameters.getGenrePhotosQty() ) );

		return services;
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

	private class GoToParameters {

		private final User accessor;
		private final int genrePhotosQty;
		private boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		private boolean photoAuthorNameMustBeHidden;

		public GoToParameters( final User accessor, final int genrePhotosQty ) {
			this.accessor = accessor;
			this.genrePhotosQty = genrePhotosQty;
		}

		public User getAccessor() {
			return accessor;
		}

		public int getGenrePhotosQty() {
			return genrePhotosQty;
		}

		public boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
			return showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		}

		public void setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
			this.showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		}

		public boolean isPhotoAuthorNameMustBeHidden() {
			return photoAuthorNameMustBeHidden;
		}

		public void setPhotoAuthorNameMustBeHidden( final boolean photoAuthorNameMustBeHidden ) {
			this.photoAuthorNameMustBeHidden = photoAuthorNameMustBeHidden;
		}
	}
}
