package photo.list.filtering;

import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.filtering.TopBestFilteringStrategy;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceTopBestTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

		final AbstractPhotoFilteringStrategy filteringStrategy = new TopBestFilteringStrategy( testData.accessor, getServices() );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

		testData.isRestricted = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new TopBestFilteringStrategy( testData.accessor, getServices() );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleEvenForAdminIfThePhotoIsRestrictedTest() {

		testData.accessor = SUPER_ADMIN_1;
		testData.isRestricted = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new TopBestFilteringStrategy( testData.accessor, getServices() );

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleEvenForPhotoAuthorIfThePhotoIsRestrictedTest() {

		testData.accessor = testData.photoAuthor;
		testData.isRestricted = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new TopBestFilteringStrategy( testData.accessor, getServices() );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

}
