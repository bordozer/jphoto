package photo.list.filtering;

import common.AbstractTestCase;
import core.services.photo.PhotoListFilteringServiceImpl;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import org.easymock.EasyMock;

public class AbstractPhotoListFilteringServiceTest_ extends AbstractTestCase {

	protected PhotoListFilteringServiceImpl getPhotoListFilteringService( final TestData testData ) {
		final PhotoListFilteringServiceImpl photoListFilteringService = new PhotoListFilteringServiceImpl();

		photoListFilteringService.setSecurityService( getSecurityService( testData ) );
		photoListFilteringService.setRestrictionService( getRestrictionService( testData ) );

		return photoListFilteringService;
	}

	protected SecurityService getSecurityService( final TestData testData ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( testData.accessor ) ).andReturn( isSuperAdmin( testData.accessor ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	protected RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		EasyMock.expect( restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn( testData.photo.getId(), testData.currentTime ) ).andReturn( testData.isRestricted ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}
}
