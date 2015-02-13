package photo.list.filtering;

import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.filtering.BestFilteringStrategy;
import core.services.photo.list.filtering.GalleryFilteringStrategy;
import org.junit.Test;

public class PhotoListFilteringServiceGalleryTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

		final AbstractPhotoFilteringStrategy filteringStrategy = new GalleryFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminEvenIfThePhotoIsRestrictedTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isRestricted = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new GalleryFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new GalleryFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForPhotoAuthorIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;
		testData.accessor = testData.photoAuthor;

		final AbstractPhotoFilteringStrategy filteringStrategy = new GalleryFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleIfPhotoAuthorIsInInvisibilityListTest() {

		testData.isPhotoAuthorInInvisibilityList = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminsEvenIfPhotoAuthorIsInInvisibilityListTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isPhotoAuthorInInvisibilityList = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
