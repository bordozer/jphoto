package menuItems.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.admin.PhotoMenuItemDeleteAdmin;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemDeleteAdminTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeDeletePhotoMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;
		doAssertFalse( accessor, getServicesForTest( new Parameters( accessor ) ) );
	}

	@Test
	public void photoAuthorCanNotSeeDeletePhotoMenuTest() {
		final User accessor = testData.getPhotoAuthor();
		doAssertFalse( accessor, getServicesForTest( new Parameters( accessor ) ) );
	}

	@Test
	public void usualUserCanNotSeeDeletePhotoMenuTest() {
		final User accessor = testData.getAccessor();
		doAssertFalse( accessor, getServicesForTest( new Parameters( accessor ) ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuIfConfigurationKeyAdminCanDeleteOtherPhotosIsOFFTest() {
		final User accessor = SUPER_ADMIN_1;
		doAssertFalse( accessor, getServicesForTest( new Parameters( accessor, false ) ) );
	}

	@Test
	public void userCanNotSeeDeletePhotoMenuForAdminTest() {
		final User accessor = testData.getAccessor();

		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_2.getId() );

		doAssertFalse( accessor, getServicesForTest( new Parameters( accessor, true ) ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuIfConfigurationKeyAdminCanDeleteOtherPhotosIsONTest() {
		final User accessor = SUPER_ADMIN_1;
		doAssertTrue( accessor, getServicesForTest( new Parameters( accessor, true ) ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuForAnotherAdminTest() {
		final User accessor = SUPER_ADMIN_1;

		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_2.getId() );

		doAssertTrue( accessor, getServicesForTest( new Parameters( accessor, true ) ) );
	}

	private ServicesImpl getServicesForTest( final Parameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );

		services.setConfigurationService( getConfigurationService( parameters.isAdminCanDeleteOtherPhotos() ) );

		return services;
	}

	private ConfigurationService getConfigurationService( final boolean adminCanDeleteOtherPhotos ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS ) ).andReturn( adminCanDeleteOtherPhotos ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private void doAssertTrue( final User accessor, final Services services ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemDeleteAdmin( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	private void doAssertFalse( final User accessor, final Services services ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemDeleteAdmin( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	private static class Parameters {

		private final User accessor;
		private boolean adminCanDeleteOtherPhotos;

		private Parameters( final User accessor ) {
			this.accessor = accessor;
		}

		private Parameters( final User accessor, final boolean adminCanDeleteOtherPhotos ) {
			this.accessor = accessor;
			this.adminCanDeleteOtherPhotos = adminCanDeleteOtherPhotos;
		}

		public User getAccessor() {
			return accessor;
		}

		public boolean isAdminCanDeleteOtherPhotos() {
			return adminCanDeleteOtherPhotos;
		}

		public void setAdminCanDeleteOtherPhotos( final boolean adminCanDeleteOtherPhotos ) {
			this.adminCanDeleteOtherPhotos = adminCanDeleteOtherPhotos;
		}
	}
}
