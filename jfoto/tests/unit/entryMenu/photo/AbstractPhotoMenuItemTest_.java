package entryMenu.photo;

import common.AbstractTestCase;
import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractPhotoMenuItemTest_ extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	protected void assertUserMenuItemAccess( final PhotoMenuItemAccessStrategy accessStrategy, final AbstractPhotoMenuItem menuItem, final PhotoInitialConditions initialConditions ) {

		final Photo photo = new Photo();
		photo.setId( initialConditions.getPhotoId() );
		photo.setUserId( initialConditions.getPhotoAuthorId() );

		final User userWhoIsCallingMenu = new User( initialConditions.getUserWhoIsCallingMenuId() );

		initialConditions.setPhoto( photo );
		initialConditions.setUserWhoIsCallingMenu( userWhoIsCallingMenu );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( initialConditions.getPhotoAuthorId(), userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isUserWhoIsCallingMenuInTheBlackList() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );
		menuItem.setFavoritesService( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( initialConditions.isShowMenuGoToForOwnEntriesSettingIsSwitchedOn() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		menuItem.setConfigurationService( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.load( initialConditions.getPhotoId() ) ).andReturn( photo ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUser( initialConditions.getPhotoAuthorId() ) ).andReturn( initialConditions.getPhotoAuthorPhotosQty() ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( initialConditions.getPhotoAuthorId(), photo.getGenreId() ) ).andReturn( initialConditions.getPhotoQtyByUserAndGenre() ).anyTimes();
		EasyMock.expect( photoService.isPhotoAuthorNameMustBeHidden( photo, initialConditions.getUserWhoIsCallingMenu() ) ).andReturn( initialConditions.isPhotoAuthorNameMustBeHidden() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		menuItem.setPhotoService( photoService );

		final User photoAuthor = new User();
		photoAuthor.setId( initialConditions.getPhotoAuthorId() );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photo.getUserId() ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );
		menuItem.setUserService( userService );

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );
		EasyMock.expect( securityService.userOwnThePhoto( userWhoIsCallingMenu, photo ) ).andReturn( initialConditions.isUserWhoIsOpeningMenuOwnerOfThePhoto() ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( userWhoIsCallingMenu.getId() ) ).andReturn( initialConditions.isMenuCallerSuperAdmin() ).anyTimes();
		EasyMock.expect( securityService.userCanDeletePhoto( userWhoIsCallingMenu, photo ) ).andReturn( initialConditions.isUserCanDeletePhoto() ).anyTimes();
		EasyMock.expect( securityService.userCanEditPhoto( userWhoIsCallingMenu, photo ) ).andReturn( initialConditions.isUserCanEditPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );
		menuItem.setSecurityService( securityService );

		final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock( UserPhotoAlbumService.class );
		EasyMock.expect( userPhotoAlbumService.getUserPhotoAlbumPhotosQty( EasyMock.anyInt() ) ).andReturn( initialConditions.getUserPhotoAlbumPhotosQty() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userPhotoAlbumService );
		menuItem.setUserPhotoAlbumService( userPhotoAlbumService );

		final UserTeamService userTeamService = EasyMock.createMock( UserTeamService.class );
		EasyMock.expect( userTeamService.getTeamMemberPhotosQty( EasyMock.anyInt() ) ).andReturn( initialConditions.getTeamMemberPhotosQty() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userTeamService );
		menuItem.setUserTeamService( userTeamService );

		accessStrategy.assertMenuItemAccess( menuItem, initialConditions );
	}
}
