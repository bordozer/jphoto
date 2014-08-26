package photo.list.filtering;

import common.AbstractTestCase;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import core.services.system.Services;
import core.services.system.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Before;

import static junit.framework.Assert.assertEquals;

public class AbstractPhotoListFilteringServiceTest_ extends AbstractTestCase {

	protected TestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new TestData( dateUtilsService );
	}

	protected Services getServices( final TestData testData ) {
		final ServicesImpl services = new ServicesImpl();

		services.setSecurityService( getSecurityService( testData ) );
		services.setRestrictionService( getRestrictionService( testData ) );
		services.setPhotoService( getPhotoService( testData ) );

		return services;
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

	protected void assertPhotoIsShown( final boolean photoIsHidden ) {
		assertEquals( "Photo is hidden but should not be", false, photoIsHidden );
	}

	protected void assertPhotoIsHidden( final boolean photoIsHidden ) {
		assertEquals( "Photo is shown but should be hidden", true, photoIsHidden );
	}
}
