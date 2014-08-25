package services;

import common.AbstractTestCase;
import core.general.configuration.ConfigurationKey;
import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.services.entry.GroupOperationServiceImpl;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import mocks.UserMock;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;

public class GroupOperationServiceTest extends AbstractTestCase {

	private static final String A_GROUP_MENU_CAN_NOT_BE_FOUND = "A group operation menu is not provided by photo list";

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void noGroupOperationMenusTest() {

		final TestData testData = new TestData();
		testData.accessor = new UserMock( 432 );

		final GroupOperationServiceImpl groupOperationService = getGroupOperationService( testData );
		final List<PhotoGroupOperationMenu> operationMenus = groupOperationService.getNoPhotoGroupOperationMenuContainer().getGroupOperationMenus();

		assertTrue( operationMenus.size() == 0 );
	}

	@Test
	public void userOwnPhotosGroupOperationMenusTest() {

		final TestData testData = new TestData();
		testData.accessor = new UserMock( 432 );

		final GroupOperationServiceImpl groupOperationService = getGroupOperationService( testData );
		final List<PhotoGroupOperationMenu> operationMenus = groupOperationService.getUserOwnPhotosGroupOperationMenus();

		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.ARRANGE_PHOTO_ALBUMS ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.ARRANGE_TEAM_MEMBERS ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.ARRANGE_NUDE_CONTENT ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.MOVE_TO_GENRE ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.DELETE_PHOTOS ) );
	}

	@Test
	public void userCommonPhotosGroupOperationMenusTest() {

		final TestData testData = new TestData();
		testData.accessor = new UserMock( 432 );

		final GroupOperationServiceImpl groupOperationService = getGroupOperationService( testData );
		final List<PhotoGroupOperationMenu> operationMenus = groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( testData.accessor ).getGroupOperationMenus();

		assertTrue( operationMenus.size() == 0 );
	}

	@Test
	public void adminCan_NOT_DeleteAnotherPhotosTest() {

		final TestData testData = new TestData();
		testData.accessor = SUPER_ADMIN_1;
		testData.isAdminCanDeleteAnotherPhotos = false;

		final GroupOperationServiceImpl groupOperationService = getGroupOperationService( testData );
		final List<PhotoGroupOperationMenu> operationMenus = groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( testData.accessor ).getGroupOperationMenus();

		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.ARRANGE_NUDE_CONTENT ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.MOVE_TO_GENRE ) );
	}

	@Test
	public void adminCanDeleteAnotherPhotosTest() {

		final TestData testData = new TestData();
		testData.accessor = SUPER_ADMIN_1;
		testData.isAdminCanDeleteAnotherPhotos = true;

		final GroupOperationServiceImpl groupOperationService = getGroupOperationService( testData );
		final List<PhotoGroupOperationMenu> operationMenus = groupOperationService.getPhotoListPhotoGroupOperationMenuContainer( testData.accessor ).getGroupOperationMenus();

		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.ARRANGE_NUDE_CONTENT ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.MOVE_TO_GENRE ) );
		assertTrue( A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu( operationMenus, PhotoGroupOperationType.DELETE_PHOTOS ) );
	}

	private GroupOperationServiceImpl getGroupOperationService( final TestData testData ) {
		final GroupOperationServiceImpl groupOperationService = new GroupOperationServiceImpl();

		groupOperationService.setSecurityService( getSecurityService( testData ) );
		groupOperationService.setConfigurationService( getConfigurationService( testData ) );

		return groupOperationService;
	}

	private SecurityService getSecurityService( final TestData testData ) {

		final SecurityService photoService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( photoService.isSuperAdminUser( testData.accessor.getId() ) ).andReturn( isSuperAdmin( testData.accessor ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private ConfigurationService getConfigurationService( final TestData testData ) {

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS ) ).andReturn( testData.isAdminCanDeleteAnotherPhotos ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private boolean containsMenu( final List<PhotoGroupOperationMenu> operationMenus, final PhotoGroupOperationType operationType ) {

		for ( final PhotoGroupOperationMenu photoGroupOperationMenu : operationMenus ) {
			if ( photoGroupOperationMenu.getPhotoGroupOperation() == operationType ) {
				return true;
			}
		}

		return false;
	}

	private static class TestData {

		User accessor;

		boolean isAdminCanDeleteAnotherPhotos;
	}
}
