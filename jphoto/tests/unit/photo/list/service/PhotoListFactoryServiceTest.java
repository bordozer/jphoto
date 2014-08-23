package photo.list.service;

import common.AbstractTestCase;
import core.general.configuration.ConfigurationKey;
import core.services.photo.list.PhotoListFactoryServiceImpl;
import core.services.photo.list.PhotoListFilteringService;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import core.services.system.ConfigurationService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryServiceTest extends AbstractTestCase {

	public static final int DAYS = 2;
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
	}

	@Test
	public void galleryForGenreTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForGenre( testData.genre, 5, 16, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build() );
	}

	@Test
	public void galleryForUserTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUser( testData.user, 3, 36, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build() );
	}

	@Test
	public void galleryForUserAndGenreTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForUserAndGenre( testData.user, testData.genre, 3, 36, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build() );
	}

	@Test
	public void userCardPhotosLastTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).userCardPhotosLast( testData.user, testData.accessor );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build() );
	}

	@Test
	public void galleryTopBestTest() {
		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryTopBest( testData.accessor );

		final Date timeFrom = dateUtilsService.getLastSecondOfDay( dateUtilsService.getCurrentTime() );
		final Date timeTo = dateUtilsService.getFirstSecondOfDay( dateUtilsService.getDatesOffsetFromCurrentDate( -DAYS ) );

		final String to1 = dateUtilsService.formatDateTime( timeFrom );
		final String from1 = dateUtilsService.formatDateTime( timeTo );

		final String to2 = dateUtilsService.formatDate( timeFrom );
		final String from2 = dateUtilsService.formatDate( timeTo );

		assertEquals( String.format( "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id ORDER BY SUM( photoVoting.mark ) DESC LIMIT 4;", from1, to1 ), factory.getSelectIdsQuery().build() );
		assertEquals( String.format( "http://127.0.0.1:8085/worker/photos/from/%s/to/%s/best/", from2, to2 ), factory.getLinkToFullList() );
	}

	private PhotoListFactoryServiceImpl getPhotoListFactoryService( final TestData testData ) {
		final PhotoListFactoryServiceImpl photoListFactoryService = new PhotoListFactoryServiceImpl();

		photoListFactoryService.setPhotoListFilteringService( getPhotoListFilteringService( testData ) );
		photoListFactoryService.setConfigurationService( getConfigurationService( testData ) );
		photoListFactoryService.setDateUtilsService( dateUtilsService );
		photoListFactoryService.setUrlUtilsService( urlUtilsService );

		return photoListFactoryService;
	}

	private ConfigurationService getConfigurationService( final TestData testData ) {

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) ).andReturn( 4 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS ) ).andReturn( DAYS ).anyTimes();

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

		EasyMock.expectLastCall();
		EasyMock.replay( photoListFilteringService );

		return photoListFilteringService;
	}
}
