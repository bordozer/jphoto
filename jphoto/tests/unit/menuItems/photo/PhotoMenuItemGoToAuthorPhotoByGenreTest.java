package menuItems.photo;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_ {

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

	private GenreService getGenreService() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( testData.getPhoto().getGenreId() ) ).andReturn( testData.getGenre() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private PhotoService getPhotoService( final int genrePhotosQty ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( testData.getPhotoAuthor().getId(), testData.getPhoto().getGenreId() ) ).andReturn( genrePhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
