package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.user.UserPhotoAlbumService;
import org.easymock.EasyMock;

public class PhotoMenuItemGoToAuthorPhotoByAlbumTest extends AbstractPhotoMenuItemTest_ {

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

	private UserPhotoAlbumService getUserPhotoAlbumService( final int photoAlbumId, final int albumPhotosQty ) {
		final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock( UserPhotoAlbumService.class );

		EasyMock.expect( userPhotoAlbumService.getUserPhotoAlbumPhotosQty( photoAlbumId ) ).andReturn( albumPhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userPhotoAlbumService );

		return userPhotoAlbumService;
	}

	private UserPhotoAlbum getUserPhotoAlbum() {
		final UserPhotoAlbum album = new UserPhotoAlbum();

		album.setId( 881 );
		album.setName( "Photo album" );

		return album;
	}
}
