package photo.list.filtering;

import core.services.photo.PhotoListFilteringServiceImpl;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.factory.AbstractPhotoFilteringStrategy;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceGalleryTest extends AbstractPhotoListFilteringServiceTest_ {

	private TestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new TestData( dateUtilsService );
	}

	@Test
	public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

		testData.isRestricted = false;
		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertEquals( "Assertion fails", false, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminEvenIfThePhotoIsRestrictedTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertEquals( "Assertion fails", false, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForPhotoAuthorIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;
		testData.accessor = testData.photoAuthor;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
