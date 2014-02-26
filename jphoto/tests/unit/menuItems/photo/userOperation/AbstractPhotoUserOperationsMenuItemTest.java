package menuItems.photo.userOperation;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.items.AbstractPhotoUserOperationsMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractPhotoUserOperationsMenuItemTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void adminCanSeeOwnPhotoMenuTest() {

		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_1.getId() );

		final Parameters parameters = new Parameters( SUPER_ADMIN_1, true, photo );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( parameters ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeOwnPhotoMenuTest() {

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), true, testData.getPhoto() );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( parameters ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {

		final Parameters parameters = new Parameters( testData.getPhotoAuthor(), true, testData.getPhoto() );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( parameters ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThisIsNOTHisPhotoTest() {

		final Parameters parameters = new Parameters( SUPER_ADMIN_1, true, testData.getPhoto() );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry( parameters ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeMenuIfHeIsNotPhotoAuthorAndDoesNotHaveAccessToEditPhotoTest() {

		final Parameters parameters = new Parameters( testData.getAccessor(), false, testData.getPhoto() );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry( parameters ).isAccessibleFor() );
	}

	private AbstractPhotoUserOperationsMenuItem getMenuEntry( final Parameters parameters ) {
		return new AbstractPhotoUserOperationsMenuItem( parameters.getPhoto(), parameters.getAccessor(), getServicesEdit( parameters.getAccessor(), parameters.isHasAccessToOperation() ) ) {

			@Override
			protected boolean hasAccessTo() {
				return parameters.isHasAccessToOperation();
			}

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null;
			}

			@Override
			public AbstractEntryMenuItemCommand getMenuItemCommand() {
				return null;
			}
		};
	}

	private ServicesImpl getServicesEdit( final User accessor, final boolean userCanEditPhoto ) {
		final ServicesImpl services = getServices( accessor );

		services.setSecurityService( getSecurityService( accessor, userCanEditPhoto ) );

		return services;
	}

	private SecurityService getSecurityService( final User accessor, final boolean userCanDeletePhoto ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userCanEditPhoto( EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject() ) ).andReturn( userCanDeletePhoto ).anyTimes();

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

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

	private static class Parameters {

		private final User accessor;
		private final boolean hasAccessToOperation;
		private final Photo photo;

		private Parameters( final User accessor, final boolean hasAccessToOperation, final Photo photo ) {
			this.accessor = accessor;
			this.hasAccessToOperation = hasAccessToOperation;
			this.photo = photo;
		}

		public User getAccessor() {
			return accessor;
		}

		public boolean isHasAccessToOperation() {
			return hasAccessToOperation;
		}

		public Photo getPhoto() {
			return photo;
		}
	}
}
