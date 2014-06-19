package rest;

import common.AbstractTestCase;
import core.enums.FavoriteEntryType;
import core.general.configuration.ConfigurationKey;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.user.UserService;
import json.photo.list.PhotoEntryDTO;
import json.photo.list.PhotoListEntryController;
import mocks.GenreMock;
import mocks.PhotoMock;
import mocks.UserMock;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import ui.services.security.SecurityUIService;

import java.util.EnumSet;

import static junit.framework.Assert.assertEquals;

public class PhotoListEntryControllerTest extends AbstractTestCase {

	public static final String THE_STRINGS_ARE_NOT_EQUAL = "The strings are not equal";

	private UserMock accessor;
	private UserMock photoAuthor;
	private PhotoMock photo;
	private GenreMock genre;

	@Before
	public void setup() {
		super.setup();

		accessor = new UserMock( 444 );
		photoAuthor = new UserMock( 321 );

		genre = new GenreMock( 555 );

		photo = new PhotoMock( 777 );
		photo.setUploadTime( dateUtilsService.getFirstSecondOfToday() );
		photo.setGenreId( genre.getId() );
	}

	@Test
	public void uploadTimeTodayTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, MENU_LANGUAGE );

		assertEquals( THE_STRINGS_ARE_NOT_EQUAL, dto.getUserId(), testData.accessor.getId() );
	}

	private PhotoListEntryController getController( final TestData testData ) {

		final PhotoListEntryController controller = new PhotoListEntryController();

		controller.setTranslatorService( translatorService );
		controller.setDateUtilsService( dateUtilsService );
		controller.setUrlUtilsService( urlUtilsService );
		controller.setEntityLinkUtilsService( entityLinkUtilsService );
		controller.setUserPhotoFilePathUtilsService( userPhotoFilePathUtilsService );

		controller.setUserService( getUserService( testData ) );
		controller.setSecurityService( getSecurityService( testData ) );
		controller.setSecurityUIService( getSecurityUIService( testData ) );
		controller.setGenreService( getGenreService() );
		controller.setConfigurationService( getConfigurationService( testData ) );
		controller.setFavoritesService( setFavoritesService( testData ) );

		return controller;
	}

	private UserService getUserService( final TestData testData ) {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.photo.getUserId() ) ).andReturn( photoAuthor ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService( final TestData testData ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.photo, testData.accessor ) ).andReturn( testData.photoAuthorNameMustBeHidden ).anyTimes();
		EasyMock.expect( securityService.userOwnThePhoto( testData.accessor, testData.photo ) ).andReturn( testData.accessor == photoAuthor ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.accessor ) ).andReturn( isSuperAdmin( testData.accessor ) ).anyTimes();
		EasyMock.expect( securityService.isPhotoWithingAnonymousPeriod( testData.photo ) ).andReturn( testData.photoWithingAnonymousPeriod ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private SecurityUIService getSecurityUIService( final TestData testData ) {
		final SecurityUIService securityUIService = EasyMock.createMock( SecurityUIService.class );

		EasyMock.expect( securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( testData.photo, testData.accessor ) ).andReturn( testData.photoHasToBeHiddenBecauseOfNudeContent ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityUIService );

		return securityUIService;
	}

	private GenreService getGenreService() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( genre.getId() ) ).andReturn( genre ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private ConfigurationService getConfigurationService( final TestData testData ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PHOTO_MENU ) ).andReturn( testData.confKeyPhotoListShowPhotoMenu ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_STATISTIC ) ).andReturn( testData.confKeyPhotoListShowStatistic ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE ) ).andReturn( testData.confKeyPhotoListShowUserRankInGenre ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private FavoritesService setFavoritesService( final TestData testData ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		final EnumSet<FavoriteEntryType> types = FavoriteEntryType.RELATED_TO_PHOTO;
		for ( final FavoriteEntryType type : types ) {
			EasyMock.expect( favoritesService.isEntryInFavorites( testData.accessor.getId(), testData.photo.getId(), type.getId() ) ).andReturn( testData.favorites != null && testData.favorites.contains( type ) ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}

	private class TestData {

		private final UserMock accessor;
		private final PhotoMock photo;

		private boolean photoHasToBeHiddenBecauseOfNudeContent;
		private boolean photoAuthorNameMustBeHidden;
		public boolean photoWithingAnonymousPeriod;

		private boolean confKeyPhotoListShowPhotoMenu;
		private boolean confKeyPhotoListShowStatistic;
		private boolean confKeyPhotoListShowUserRankInGenre;

		private EnumSet<FavoriteEntryType> favorites;

		private TestData( final PhotoMock photo, final UserMock accessor ) {
			this.accessor = accessor;
			this.photo = photo;
		}
	}
}
