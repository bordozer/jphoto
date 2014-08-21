package photo.list.filtering;

import core.services.photo.PhotoListFilteringServiceImpl;
import org.junit.Test;
import ui.services.photo.listFactory.factory.AbstractPhotoFilteringStrategy;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceTopBestTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.topBestFilteringStrategy();

		assertEquals( "Assertion fails", false, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.topBestFilteringStrategy();

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleEvenForAdminIfThePhotoIsRestrictedTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.topBestFilteringStrategy();

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleEvenForPhotoAuthorIfThePhotoIsRestrictedTest() {

		testData.accessor = testData.photoAuthor;
		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.topBestFilteringStrategy();

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
