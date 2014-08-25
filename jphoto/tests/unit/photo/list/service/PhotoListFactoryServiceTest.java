package photo.list.service;

import common.AbstractTestCase;
import core.general.configuration.ConfigurationKey;
import core.general.data.TimeRange;
import core.general.photo.PhotoVotingCategory;
import core.general.user.UserMembershipType;
import core.services.photo.PhotoVotingService;
import core.services.photo.list.PhotoListFactoryServiceImpl;
import core.services.photo.list.PhotoListFilteringService;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import core.services.system.ConfigurationService;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import mocks.PhotoVotingCategoryMock;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryServiceTest extends AbstractTestCase {

	private static final int DAYS = 2;
	private static final int MIN_MARKS_FOR_VERY_BEST = 40;

	TestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new TestData();
	}

	@Test
	public void galleryTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).gallery( 5, 24, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 96;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
//		assertEquals( StringUtils.EMPTY, factory.getPhotoList( 0, 1, Language.EN, dateUtilsService.parseDateTime( "2014-08-24 15:39:40" ) ).getLinkToFullList() ); // TODO: check this
	}

	@Test
	public void galleryForGenreTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForGenre( testData.genre, 5, 16, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a>", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUser( testData.user, 3, 36, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserAndGenreTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserAndGenre( testData.user, testData.genre, 3, 36, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Accessor: user card link title\">Accessor</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a>", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void userCardPhotosLastTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).userCardPhotosLast( testData.user, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: User card <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: the latest photos", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/members/112/", factory.getLinkToFullList() );
	}

	@Test
	public void galleryTopBestTest() {
		final DateData dateData = new DateData();

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryTopBest( testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '40' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 4;", dateData.from1, dateData.to1 ), factory.getSelectIdsQuery().build() );
		assertEquals( String.format( "Photo list title: Photo gallery top best for last %s days", DAYS ), factory.getTitle().build( Language.EN ) );
		assertEquals( String.format( "http://127.0.0.1:8085/worker/photos/from/%s/to/%s/best/", dateData.from2, dateData.to2 ), factory.getLinkToFullList() );
	}

	@Test
	public void galleryAbsolutelyBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryAbsolutelyBest( 2, 12, testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 12 OFFSET 12;", MIN_MARKS_FOR_VERY_BEST ), factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery absolutely best", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryForGenreBestTest() {
		final DateData dateData = new DateData();

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForGenreBest( testData.genre, 2, 12, testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 12 OFFSET 12;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST ), factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> best for 2 days", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserTopBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserTopBest( testData.user, 5, 20, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> top best", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/members/112/best/", factory.getLinkToFullList() );
	}

	@Test
	public void userCardPhotosBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).userCardPhotosBest( testData.user, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: User card <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: the best photos", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/members/112/best/", factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserBest( testData.user, 1, 36, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 36;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> best", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserAndGenreTopBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserAndGenreTopBest( testData.user, testData.genre, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> top best", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/members/112/genre/222/best/", factory.getLinkToFullList() );
	}

	@Test
	public void galleryForUserAndGenreBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserAndGenreBest( testData.user, testData.genre, 5, 20, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 20 OFFSET 80;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> best", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void userCardPhotosLastAppraisedTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).userCardPhotosLastAppraised( testData.user, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '112' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: User card <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: last appraised photos", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/members/112/category/", factory.getLinkToFullList() );
	}

	@Test
	public void appraisedByUserPhotosTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).appraisedByUserPhotos( testData.user, 10, 24, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '112' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void appraisedByUserPhotosByVotingCategoryTest() {
		final PhotoVotingCategory votingCategory = new PhotoVotingCategoryMock( 543 );
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).appraisedByUserPhotos( testData.user, votingCategory, 10, 24, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.userId = '112' ) AND photoVoting.votingCategoryId = '543' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryUploadedInDateRangeTest() {
		final DateData dateData = new DateData();

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryUploadedInDateRange( dateUtilsService.parseDateTime( "2014-08-10 12:15:48" ), dateUtilsService.parseDateTime( "2014-08-14 03:42:15" ), 10, 24, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( ( photos.uploadTime >= '2014-08-10 00:00:00' ) AND photos.uploadTime <= '2014-08-14 23:59:59' ) ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: Photos uploaded between 2014-08-10 and 2014-08-14", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryUploadedInDateRangeBestTest() {
		final DateData dateData = new DateData();

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryUploadedInDateRangeBest( dateData.timeFrom, dateData.timeTo, 10, 24, testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 24 OFFSET 216;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST ), factory.getSelectIdsQuery().build() );
		assertEquals( "Photo list title: The best photos for period 2014-08-25 - 2014-08-24", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryByUserMembershipTypeTest() {

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryByUserMembershipType( UserMembershipType.MODEL, 10, 24, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE ( users.membershipType = '2' ) ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build() );
		assertEquals( "Main menu: photos: UserMembershipType: model", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	@Test
	public void galleryByUserMembershipTypeTopBestTest() {
		final DateData dateData = new DateData();
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryByUserMembershipTypeTopBest( UserMembershipType.MAKEUP_MASTER, testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) INNER JOIN users ON ( photos.userId = users.id ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND users.membershipType = '3' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 4;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST ), factory.getSelectIdsQuery().build() );
		assertEquals( "Main menu: The best photos: UserMembershipType: makeup master", factory.getTitle().build( Language.EN ) );
		assertEquals( "http://127.0.0.1:8085/worker/photos/type/3/best/", factory.getLinkToFullList() );
	}

	@Test
	public void galleryByUserMembershipTypeBestTest() {
		final DateData dateData = new DateData();
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryByUserMembershipTypeBest( UserMembershipType.MAKEUP_MASTER, 5, 16, testData.accessor );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) INNER JOIN users ON ( photos.userId = users.id ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND users.membershipType = '3' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 16 OFFSET 64;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST ), factory.getSelectIdsQuery().build() );
		assertEquals( "Main menu: The best photos: UserMembershipType: makeup master", factory.getTitle().build( Language.EN ) );
		assertEquals( StringUtils.EMPTY, factory.getLinkToFullList() );
	}

	private PhotoListFactoryServiceImpl getPhotoListFactoryService( final TestData testData ) {
		final ConfigurationService configurationService = getConfigurationService( testData );

		final PhotoListFactoryServiceImpl photoListFactoryService = new PhotoListFactoryServiceImpl();

		final ServicesImpl services = getServices();
		services.setConfigurationService( configurationService );
		services.setPhotoVotingService( getPhotoVotingService() );
		services.setUrlUtilsService( urlUtilsService );

		photoListFactoryService.setServices( services );
		photoListFactoryService.setPhotoListFilteringService( getPhotoListFilteringService( testData ) );
//		photoListFactoryService.setConfigurationService( configurationService );
		photoListFactoryService.setDateUtilsService( dateUtilsService );

		return photoListFactoryService;
	}

	private PhotoVotingService getPhotoVotingService() {
		final TimeRange timeRange = new TimeRange( dateUtilsService.getFirstSecondOfDay( dateUtilsService.getDatesOffsetFromCurrentDate( -DAYS + 1 ) ), dateUtilsService.getLastSecondOfToday() );

		final PhotoVotingService photoVotingService = EasyMock.createMock( PhotoVotingService.class );

		EasyMock.expect( photoVotingService.getTopBestDateRange() ).andReturn( timeRange ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoVotingService );

		return photoVotingService;
	}

	private ConfigurationService getConfigurationService( final TestData testData ) {

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) ).andReturn( 4 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS ) ).andReturn( DAYS ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO ) ).andReturn( MIN_MARKS_FOR_VERY_BEST ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private PhotoListFilteringService getPhotoListFilteringService( final TestData testData ) {

		final PhotoListFilteringService photoListFilteringService = EasyMock.createMock( PhotoListFilteringService.class );

		final AbstractPhotoFilteringStrategy filteringStrategy = new AbstractPhotoFilteringStrategy() {
			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {
				return false;
			}
		};

		EasyMock.expect( photoListFilteringService.galleryFilteringStrategy( testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.topBestFilteringStrategy() ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.userCardFilteringStrategy( testData.user, testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();
		EasyMock.expect( photoListFilteringService.bestFilteringStrategy( testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListFilteringService );

		return photoListFilteringService;
	}

	private class DateData {

		final Date timeFrom = dateUtilsService.getLastSecondOfDay( dateUtilsService.getCurrentTime() );
		final Date timeTo = dateUtilsService.getFirstSecondOfDay( dateUtilsService.getDatesOffsetFromCurrentDate( -DAYS + 1 ) );

		final String to1 = dateUtilsService.formatDateTime( timeFrom );
		final String from1 = dateUtilsService.formatDateTime( timeTo );

		final String to2 = dateUtilsService.formatDate( timeFrom );
		final String from2 = dateUtilsService.formatDate( timeTo );
	}
}