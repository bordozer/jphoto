package photo.list.service;

import common.AbstractTestCase;
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


		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).gallery( 5, 16, new UserMock( 111 ) );

		assertEquals( "SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build() );
	}

	@Test
	public void galleryForGenreTest() {
		final TestData testData = new TestData();

		final AbstractPhotoListFactory factory = getPhotoListFactoryService( testData ).galleryForGenre( testData.genre, 5, 16, new UserMock( 111 ) );

		assertEquals( "SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build() );
	}

	private PhotoListFactoryServiceImpl getPhotoListFactoryService( final TestData testData ) {
		final PhotoListFactoryServiceImpl photoListFactoryService = new PhotoListFactoryServiceImpl();

		photoListFactoryService.setPhotoListFilteringService( getPhotoListFilteringService( testData ) );

		return photoListFactoryService;
	}

	private PhotoListFilteringService getPhotoListFilteringService( final TestData testData ) {
		final PhotoListFilteringService photoListFilteringService = EasyMock.createMock( PhotoListFilteringService.class );

		EasyMock.expect( photoListFilteringService.galleryFilteringStrategy( testData.accessor ) ).andReturn( new AbstractPhotoFilteringStrategy() {
			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {
				return false;
			}
		} ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListFilteringService );

		return photoListFilteringService;
	}
}
