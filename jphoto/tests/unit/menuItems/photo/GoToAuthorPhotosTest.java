package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.items.AbstractGoToAuthorPhotos;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoTest() {
		goAssertFalse( new GoToParameters( User.NOT_LOGGED_USER, 1 ) );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {
		goAssertFalse( new GoToParameters( testData.getPhotoAuthor(), 2 ) );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 1 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		goAssertFalse( goToParameters );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );
		goToParameters.setPhotoAuthorNameMustBeHidden( true );

		goAssertFalse( goToParameters );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosTest() {
		goAssertFalse( new GoToParameters( testData.getAccessor(), 1 ) );
	}

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosTest() {
		doAssertTrue( new GoToParameters( User.NOT_LOGGED_USER, 2 ) );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosTest() {
		doAssertTrue( new GoToParameters( SUPER_ADMIN_1, 2 ) );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosTest() {
		doAssertTrue( new GoToParameters( SUPER_ADMIN_1, 2 ) );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		doAssertTrue( goToParameters );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosTest() {
		doAssertTrue( new GoToParameters( testData.getAccessor(), 2 ) );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters goToParameters ) {

		final ServicesImpl services = getServices( goToParameters.getAccessor() );

		services.setConfigurationService( getConfigurationServiceGoTo( goToParameters.isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() ) );
		services.setSecurityService( getSecurityServiceGoTo( goToParameters.getAccessor(), goToParameters.isPhotoAuthorNameMustBeHidden() ) );

		return services;
	}


	private AbstractGoToAuthorPhotos getMenuEntry( final GoToParameters goToParameters ) {
		return new AbstractGoToAuthorPhotos( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ) ) {

			@Override
			protected int getPhotosQty() {
				return goToParameters.getPhotosQty();
			}

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null; // does not matter
			}

			@Override
			public AbstractEntryMenuItemCommand getMenuItemCommand() {
				return null; // does not matter
			}
		};
	}

	private ConfigurationService getConfigurationServiceGoTo( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private SecurityService getSecurityServiceGoTo( final User accessor, final boolean isPhotoAuthorNameMustBeHidden ) {

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.getPhoto(), accessor ) ).andReturn( isPhotoAuthorNameMustBeHidden ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private void doAssertTrue( final GoToParameters goToParameters ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( goToParameters ).isAccessibleFor() );
	}

	private void goAssertFalse( final GoToParameters goToParameters ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry( goToParameters ).isAccessibleFor() );
	}

	private class GoToParameters {

		private final User accessor;
		private final int photosQty;
		private boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		private boolean photoAuthorNameMustBeHidden;

		public GoToParameters( final User accessor, final int photosQty ) {
			this.accessor = accessor;
			this.photosQty = photosQty;
		}

		public User getAccessor() {
			return accessor;
		}

		public int getPhotosQty() {
			return photosQty;
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
