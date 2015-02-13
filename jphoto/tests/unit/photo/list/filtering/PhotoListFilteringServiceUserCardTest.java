package photo.list.filtering;

import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.filtering.HideAnonymousPhotosFilteringStrategy;
import org.junit.Test;

public class PhotoListFilteringServiceUserCardTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfPhotoNotWithinAnonymousPeriodTest() {

		final AbstractPhotoFilteringStrategy filteringStrategy = new HideAnonymousPhotosFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoAuthorHidden = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new HideAnonymousPhotosFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
