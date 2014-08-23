package photo.list.service;

import common.AbstractTestCase;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoListCriteriasServiceImpl;
import core.services.photo.list.PhotoListFactoryServiceImpl;
import core.services.photo.list.PhotoListFilteringService;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import mocks.UserMock;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryServiceTest extends AbstractTestCase {

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

	private PhotoListFactoryServiceImpl getPhotoListFactoryService( final TestData testData ) {
		final PhotoListFactoryServiceImpl photoListFactoryService = new PhotoListFactoryServiceImpl();

		photoListFactoryService.setPhotoListFilteringService( getPhotoListFilteringService( testData ) );

		return photoListFactoryService;
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
		EasyMock.expect( photoListFilteringService.userCardFilteringStrategy( testData.user, testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();
//		EasyMock.expect( photoListFilteringService.userCardFilteringStrategy( testData.user, testData.accessor ) ).andReturn( filteringStrategy ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListFilteringService );

		return photoListFilteringService;
	}
}
