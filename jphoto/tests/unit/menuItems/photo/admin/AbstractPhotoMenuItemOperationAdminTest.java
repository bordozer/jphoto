package menuItems.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.admin.AbstractPhotoMenuItemOperationAdmin;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import core.services.system.ServicesImpl;
import core.services.system.ConfigurationService;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractPhotoMenuItemOperationAdminTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeDeletePhotoMenuTest() {
		final Parameters parameters = new Parameters( NOT_LOGGED_USER );
		doAssertFalse( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void photoAuthorCanNotSeeDeletePhotoMenuTest() {
		final Parameters parameters = new Parameters( testData.getPhotoAuthor() );
		doAssertFalse( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void usualUserCanNotSeeDeletePhotoMenuTest() {
		final Parameters parameters = new Parameters( testData.getAccessor() );
		doAssertFalse( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuIfConfigurationKeyAdminCanDeleteOtherPhotosIsOFFTest() {
		final Parameters parameters = new Parameters( SUPER_ADMIN_1, false );
		doAssertFalse( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void userCanNotSeeDeletePhotoMenuForAdminTest() {
		final Parameters parameters = new Parameters( testData.getAccessor(), true );

		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_2.getId() );

		doAssertFalse( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuIfConfigurationKeyAdminCanDeleteOtherPhotosIsONTest() {
		final Parameters parameters = new Parameters( SUPER_ADMIN_1, true );
		doAssertTrue( parameters, getServicesForTest( parameters ) );
	}

	@Test
	public void adminCanSeeDeletePhotoMenuForAnotherAdminTest() {
		final Parameters parameters = new Parameters( SUPER_ADMIN_1, true );

		final Photo photo = testData.getPhoto();
		photo.setUserId( SUPER_ADMIN_2.getId() );

		doAssertTrue( parameters, getServicesForTest( parameters ) );
	}

	private ServicesImpl getServicesForTest( final Parameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );

		services.setConfigurationService( getConfigurationService( parameters.isOperationConfigurationOn() ) );

		return services;
	}

	private ConfigurationService getConfigurationService( final boolean adminCanDeleteOtherPhotos ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS ) ).andReturn( adminCanDeleteOtherPhotos ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private void doAssertTrue( final Parameters parameters, final Services services ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry( parameters, services ).isAccessibleFor() );
	}

	private void doAssertFalse( final Parameters parameters, final Services services ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry( parameters, services ).isAccessibleFor() );
	}

	private AbstractPhotoMenuItemOperationAdmin getMenuEntry( final Parameters parameters, final Services services ) {
		return new AbstractPhotoMenuItemOperationAdmin( testData.getPhoto(), parameters.getAccessor(), services ) {

			@Override
			protected boolean isOperationConfigurationOn() {
				return parameters.isOperationConfigurationOn();
			}

			@Override
			public EntryMenuOperationType getEntryMenuType() {
				return null; // does not matter
			}

			@Override
			public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
				return null; // does not matter
			}
		};
	}

	private static class Parameters {

		private final User accessor;
		private boolean operationConfigurationOn;

		private Parameters( final User accessor ) {
			this.accessor = accessor;
		}

		private Parameters( final User accessor, final boolean operationConfigurationOn ) {
			this.accessor = accessor;
			this.operationConfigurationOn = operationConfigurationOn;
		}

		public User getAccessor() {
			return accessor;
		}

		public boolean isOperationConfigurationOn() {
			return operationConfigurationOn;
		}

		public void setOperationConfigurationOn( final boolean operationConfigurationOn ) {
			this.operationConfigurationOn = operationConfigurationOn;
		}
	}
}
