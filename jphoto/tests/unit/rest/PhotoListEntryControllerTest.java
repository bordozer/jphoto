package rest;

import common.AbstractTestCase;
import core.enums.FavoriteEntryType;
import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoPreviewService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.user.UserRankService;
import core.services.user.UserService;
import json.photo.list.PhotoEntryDTO;
import json.photo.list.PhotoListEntryController;
import mocks.GenreMock;
import mocks.PhotoMock;
import mocks.UserMock;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import ui.services.security.SecurityUIService;
import ui.userRankIcons.UserRankIconContainer;

import java.util.Date;
import java.util.EnumSet;

import static junit.framework.Assert.assertEquals;

public class PhotoListEntryControllerTest extends AbstractTestCase {

	private static final String THE_VALUES_ARE_NOT_EQUAL = "The values are not equal";
	private static final String ANONYMOUS_USER_NAME = "ANONYMKA";

	private static final Language LANGUAGE = MENU_LANGUAGE;

	private UserMock accessor;
	private UserMock photoAuthor;
	private PhotoMock photo;
	private GenreMock genre;

	@Before
	public void setup() {
		super.setup();

		accessor = new UserMock( 444 );
		accessor.setName( "Accessor" );

		photoAuthor = new UserMock( 321 );
		photoAuthor.setName( "Photo Author" );

		genre = new GenreMock( 555 );

		photo = new PhotoMock( 777 );
		photo.setUserId( photoAuthor.getId() );
		photo.setGenreId( genre.getId() );
		photo.setUploadTime( dateUtilsService.getCurrentTime() );
	}

	@Test
	public void accessorTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, dto.getUserId(), testData.accessor.getId() );
	}

	@Test
	public void photoTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, dto.getPhotoId(), testData.photo.getId() );
	}

	@Test
	public void groupOperationCheckboxIsControlledFromListIncludingPlaceSoItIsAlwaysAddedTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, dto.getGroupOperationCheckbox(), "<input type='checkbox' id='selectedPhotoIds' name='selectedPhotoIds' class='selectedPhotoIds' value='777' />" );
	}

	@Test
	public void photoUploadDateFiItIsUploadedTodayTest() {

		final Date firstSecondOfToday = dateUtilsService.getFirstSecondOfToday();
		photo.setUploadTime( dateUtilsService.getTimeOffsetInMinutes( firstSecondOfToday, 15 ) );
		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL
			, String.format( "<a href='http://127.0.0.1:8085/worker/photos/date/%1$s/uploaded/' title='Photo preview: show all photos uploaded at %1$s'>00:15</a>", dateUtilsService.formatDate( firstSecondOfToday ) )
			, dto.getPhotoUploadDate()
		);
	}

	@Test
	public void photoUploadDateFiItIsUploadedThreeDaysAgoTest() {

		photo.setUploadTime( dateUtilsService.parseDateTime( "20014-05-22", "14:44:23" ) );
		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a href='http://127.0.0.1:8085/worker/photos/date/20014-05-22/uploaded/' title='Photo preview: show all photos uploaded at 20014-05-22'>20014-05-22 14:44</a>", dto.getPhotoUploadDate() );
	}

	@Test
	public void photoCategoryTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/555/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a>", dto.getPhotoCategory() );
	}

	@Test
	public void photoImageTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a href='http://127.0.0.1:8085/worker/photos/777/card/' title='Photo #777'><img src='http://127.0.0.1:8085/worker/download/photos/777/preview/' class='photo-preview-image'/></a>", dto.getPhotoImage() );
	}

	@Test
	public void photoPreviewShouldBeHiddenForUsualUserIfParameterIsTRUETest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, true, LANGUAGE );  // photoPreviewShouldBeHidden = TRUE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<img src='http://127.0.0.1:8085/worker/images/hidden_picture.png' class='photo-preview-image' title='Photo preview: The photo is within anonymous period'/>", dto.getPhotoImage() );
	}

	@Test
	public void photoPreviewShouldNotBeHiddenForUsualUserIfParameterIsFalseTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE ); // photoPreviewShouldBeHidden = FALSE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a href='http://127.0.0.1:8085/worker/photos/777/card/' title='Photo #777'><img src='http://127.0.0.1:8085/worker/download/photos/777/preview/' class='photo-preview-image'/></a>", dto.getPhotoImage() );
	}

	@Test
	public void photoPreviewShouldBeReplacedForUsualUserIfThePhotoHasNudeContentTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.photoHasToBeHiddenBecauseOfNudeContent = true; // Nude content

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a href='http://127.0.0.1:8085/worker/photos/777/card/' title='Photo #777 ( Photo preview: Nude content )'><img src='http://127.0.0.1:8085/worker/images/nude_content.jpg' class='photo-preview-image block-border'/></a>", dto.getPhotoImage() );
	}

	@Test
	public void photoNameShouldBeShownTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE ); // doesPreviewHasToBeHidden == FALSE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a class=\"photo-link\" href=\"http://127.0.0.1:8085/worker/photos/777/card/\" title=\"EntityLinkUtilsService: Photo #777: photo card link title\">Photo #777</a>", dto.getPhotoName() );
	}

	@Test
	public void photoNameShouldBeReplacedIfPreviewHasToBeHiddenTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, true, LANGUAGE ); // doesPreviewHasToBeHidden == TRUE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "Photo preview: Photo's name is hidden", dto.getPhotoName() );
	}

	@Test
	public void photoAuthorLinkShouldBeShownTest() {

		final TestData testData = new TestData( photo, accessor );

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE ); // doesPreviewHasToBeHidden == FALSE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/321/card/\" title=\"EntityLinkUtilsService: Photo Author: user card link title\">Photo Author</a>", dto.getPhotoAuthorLink() );
	}

	@Test
	public void photoAuthorLinkShouldBeReplacedIfPreviewHasToBeHiddenTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.photoAuthorNameMustBeHidden = true; // TRUE
		testData.photoAnonymousPeriodExpirationTime = dateUtilsService.getTimeOffsetInMinutes( dateUtilsService.getCurrentTime(), 15 ); // Anonymous period is expiring in 15 minutes

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, ANONYMOUS_USER_NAME, dto.getPhotoAuthorLink() );
	}

	@Test
	public void photoAuthorRankShouldNotBeShownIdItIsSwitchedOffInSettingsTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.confKeyPhotoListShowUserRankInGenre = false; // FALSE

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, null, dto.getPhotoAuthorRank() );
	}

	@Test
	public void totalMarksShouldBeShownAsLinkIfPreviewHasNotToBeHiddenTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.confKeyPhotoListShowStatistic = true; // TRUE

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, LANGUAGE ); // doesPreviewHasToBeHidden = FALSE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<a href='http://127.0.0.1:8085/worker/photos/777/marks/' title='Photo preview: The photo's total marks'>43</a>", dto.getTotalMarks() );
	}

	@Test
	public void totalMarksShouldBeShownAsTextIfPreviewHasToBeHiddenTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.confKeyPhotoListShowStatistic = true; // TRUE

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, true, LANGUAGE ); // doesPreviewHasToBeHidden = TRUE

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "<span title='Photo preview: The photo's total marks'>43</span>", dto.getTotalMarks() );
	}

	@Test
	public void periodMarksTitleTest() {

		final TestData testData = new TestData( photo, accessor );
		testData.confKeyPhotoListShowStatistic = true; // TRUE

		final PhotoListEntryController controller = getController( testData );
		final PhotoEntryDTO dto = controller.photoListEntry( testData.photo, testData.accessor, false, Language.NERD );

		assertEquals( THE_VALUES_ARE_NOT_EQUAL, "Photo preview: The photo's marks for period from $1 to $2", dto.getPeriodMarksTitle() );
	}

	private PhotoListEntryController getController( final TestData testData ) {

		final PhotoListEntryController controller = new PhotoListEntryController();

		controller.setTranslatorService( translatorService );
		controller.setDateUtilsService( dateUtilsService );
		controller.setUrlUtilsService( urlUtilsService );
		controller.setEntityLinkUtilsService( entityLinkUtilsService );
		controller.setUserPhotoFilePathUtilsService( userPhotoFilePathUtilsService );

		controller.setUserService( getUserService( testData ) );
		controller.setPhotoService( getPhotoService( testData ) );
		controller.setSecurityService( getSecurityService( testData ) );
		controller.setSecurityUIService( getSecurityUIService( testData ) );
		controller.setGenreService( getGenreService() );
		controller.setConfigurationService( getConfigurationService( testData ) );
		controller.setFavoritesService( setFavoritesService( testData ) );
		controller.setPhotoVotingService( setPhotoVotingService( testData ) );
		controller.setPhotoPreviewService( getPhotoPreviewService( testData ) );
		controller.setPhotoCommentService( getPhotoCommentService( testData ) );

		return controller;
	}

	private UserService getUserService( final TestData testData ) {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.photo.getUserId() ) ).andReturn( photoAuthor ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private PhotoService getPhotoService( final TestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.photo.getId() ) ).andReturn( testData.photo ).anyTimes();
		EasyMock.expect( photoService.getPhotoAnonymousPeriodExpirationTime( testData.photo ) ).andReturn( testData.photoAnonymousPeriodExpirationTime ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
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
		EasyMock.expect( configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME ) ).andReturn( ANONYMOUS_USER_NAME ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS ) ).andReturn( 2 ).anyTimes();

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

	private PhotoVotingService setPhotoVotingService( final TestData testData ) {
		final PhotoVotingService photoVotingService = EasyMock.createMock( PhotoVotingService.class );

		EasyMock.expect( photoVotingService.getPhotoMarksForPeriod( EasyMock.anyInt(), EasyMock.anyObject( Date.class ), EasyMock.anyObject( Date.class ) ) ).andReturn( 21 ).anyTimes();
		EasyMock.expect( photoVotingService.getSummaryPhotoMark( testData.photo ) ).andReturn( 43 ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoVotingService );

		return photoVotingService;
	}

	private PhotoPreviewService getPhotoPreviewService( final TestData testData ) {
		final PhotoPreviewService photoPreviewService = EasyMock.createMock( PhotoPreviewService.class );

		EasyMock.expect( photoPreviewService.getPreviewCount( testData.photo.getId() ) ).andReturn( 143 ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoPreviewService );

		return photoPreviewService;
	}

	private PhotoCommentService getPhotoCommentService( final TestData testData ) {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.getPhotoCommentsCount( testData.photo.getId() ) ).andReturn( 67 ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
	}

	private class TestData {

		private final User accessor;

		private final PhotoMock photo;
		public Date photoAnonymousPeriodExpirationTime;

		private boolean photoHasToBeHiddenBecauseOfNudeContent;
		private boolean photoAuthorNameMustBeHidden;

		public boolean photoWithingAnonymousPeriod;
		private boolean confKeyPhotoListShowPhotoMenu;
		private boolean confKeyPhotoListShowStatistic;

		private boolean confKeyPhotoListShowUserRankInGenre;
		private EnumSet<FavoriteEntryType> favorites;

		private TestData( final PhotoMock photo, final User accessor ) {
			this.accessor = accessor;
			this.photo = photo;
		}
	}
}
