package entryMenu.user;

import common.AbstractTestCase;
import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.general.menus.user.AbstractUserMenuItem;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractUserMenuItemTest_ extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	protected void assertUserMenuItemAccess( final UserMenuItemAccessStrategy accessStrategy, final AbstractUserMenuItem menuItem, final UserInitialConditions initialConditions ) {

		final User user = new User( initialConditions.getUserId() );
		final User userWhoIsCallingMenu = new User( initialConditions.getUserWhoIsCallingMenuId() );

		initialConditions.setUser( user );
		initialConditions.setUserWhoIsCallingMenu( userWhoIsCallingMenu );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( user.getId(), userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isUserWhoIsCallingMenuInTheBlackList() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );
		menuItem.setFavoritesService( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( initialConditions.isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		menuItem.setConfigurationService( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoQtyByUser( user.getId() ) ).andReturn( initialConditions.getUserPhotosQty() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		menuItem.setPhotoService( photoService );

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );
		EasyMock.expect( securityService.isSuperAdminUser( userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isMenuCallerSuperAdmin() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );
		menuItem.setSecurityService( securityService );

		accessStrategy.assertMenuItemAccess( menuItem, initialConditions );
	}
}
