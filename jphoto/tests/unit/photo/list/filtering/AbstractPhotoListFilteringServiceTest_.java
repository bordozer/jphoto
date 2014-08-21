package photo.list.filtering;

import common.AbstractTestCase;
import core.services.photo.list.PhotoListFilteringServiceImpl;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractPhotoListFilteringServiceTest_ extends AbstractTestCase {

	protected TestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new TestData( dateUtilsService );
	}

	protected PhotoListFilteringServiceImpl getPhotoListFilteringService( final TestData testData ) {
		final PhotoListFilteringServiceImpl photoListFilteringService = new PhotoListFilteringServiceImpl();

		photoListFilteringService.setSecurityService( getSecurityService( testData ) );
		photoListFilteringService.setRestrictionService( getRestrictionService( testData ) );
		photoListFilteringService.setPhotoService( getPhotoService( testData ) );

		return photoListFilteringService;
	}

	private SecurityService getSecurityService( final TestData testData ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( testData.accessor ) ).andReturn( isSuperAdmin( testData.accessor ) ).anyTimes();
		EasyMock.expect( securityService.userOwnThePhoto( testData.accessor, testData.photo ) ).andReturn( testData.photo.getUserId() == testData.accessor.getId() ).anyTimes();
		EasyMock.expect( securityService.isPhotoWithingAnonymousPeriod( testData.photo ) ).andReturn( testData.isPhotoWithingAnonymousPeriod ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		EasyMock.expect( restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn( testData.photo.getId(), testData.currentTime ) ).andReturn( testData.isRestricted ).anyTimes();
		EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( testData.photo.getId(), testData.currentTime ) ).andReturn( testData.isRestricted ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	private  PhotoService getPhotoService( final TestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.photo.getId() ) ).andReturn( testData.photo ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
