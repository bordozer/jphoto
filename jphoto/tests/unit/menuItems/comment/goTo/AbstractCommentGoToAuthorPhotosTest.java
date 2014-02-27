package menuItems.comment.goTo;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.goTo.AbstractCommentGoToAuthorPhotos;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractCommentGoToAuthorPhotosTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInvisibleForCommentAuthorIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {

		final Parameters parameters = new Parameters( testData.getCommentAuthor(), 2 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( false );

		doAssertFalse( parameters );
	}

	@Test
	public void menuIsVisibleForCommentAuthorIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONTest() {

		final Parameters parameters = new Parameters( testData.getCommentAuthor(), 2 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		doAssertTrue( parameters );
	}

	@Test
	public void userCanNotSeeMenuIfPhotoIsWithinAnonymousPeriodTest() {
		final Parameters parameters = new Parameters( testData.getAccessor(), 2 );
		parameters.setCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( true );

		doAssertFalse( parameters );
	}

	@Test
	public void userCanSeeMenuIfPhotoIsNOTWithinAnonymousPeriodTest() {
		final Parameters parameters = new Parameters( testData.getAccessor(), 2 );
		parameters.setCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( false );

		doAssertTrue( parameters );
	}

	@Test
	public void menuIsInaccessibleIfCommentAuthorOwnThePhotoAndHasLessThenTwoPhotosTest() {

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( testData.getPhotoAuthor() );

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), 1 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		doAssertFalse( parameters );
	}

	@Test
	public void menuIsAccessibleIfCommentAuthorOwnThePhotoAndHasMoreThenOnePhotosTest() {

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( testData.getPhotoAuthor() );

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), 2 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		doAssertTrue( parameters );
	}

	@Test
	public void menuIsInaccessibleIfCommentAuthorDoesNotOwnThePhotoAndHasNoPhotosTest() {

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), 0 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( false );

		doAssertFalse( parameters );
	}

	@Test
	public void menuIsAccessibleIfCommentAuthorDoesNotOwnThePhotoAndHasAtLeastOnePhotoTest() {

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), 1 );
		parameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( false );

		doAssertTrue( parameters );
	}

	private AbstractCommentGoToAuthorPhotos getMenuEntry( final Parameters parameters ) {
		return new AbstractCommentGoToAuthorPhotos( testData.getComment(), parameters.getAccessor(), getServices( parameters ) ) {

			@Override
			public int getPhotoQty() {
				return parameters.getPhotosQty();
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


	private ServicesImpl getServices( final Parameters parameters ) {

		final ServicesImpl services = getServices( parameters.getAccessor() );

		services.setConfigurationService( getConfigurationServiceGoTo( parameters ) );
		services.setSecurityService( getSecurityService( parameters ) );

		return services;
	}

	private ConfigurationService getConfigurationServiceGoTo( final Parameters parameters ) {
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = parameters.isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn();
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private SecurityService getSecurityService( final Parameters parameters ) {
		final User accessor = parameters.getAccessor();
		final boolean isPhotoAuthorNameMustBeHidden = parameters.isPhotoAuthorNameMustBeHidden();
		final boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod = parameters.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod();

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.getPhoto(), accessor ) ).andReturn( isPhotoAuthorNameMustBeHidden ).anyTimes();

		EasyMock.expect( securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( testData.getComment(), accessor ) ).andReturn( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private void doAssertTrue( final Parameters parameters ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( parameters ).isAccessibleFor() );
	}

	private void doAssertFalse( final Parameters parameters ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry( parameters ).isAccessibleFor() );
	}

	private class Parameters {

		private final User accessor;
		private final int photosQty;
		private boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		private boolean photoAuthorNameMustBeHidden;
		private boolean commentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod;

		public Parameters( final User accessor, final int photosQty ) {
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

		public boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() {
			return commentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod;
		}

		public void setCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( final boolean commentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ) {
			this.commentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod = commentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod;
		}
	}
}
