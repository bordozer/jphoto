package photo.list.filtering;

import core.services.photo.list.PhotoListFilteringServiceImpl;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import org.junit.Test;

public class PhotoListFilteringServiceGalleryTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminEvenIfThePhotoIsRestrictedTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForPhotoAuthorIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;
		testData.accessor = testData.photoAuthor;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.galleryFilteringStrategy( testData.accessor );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
