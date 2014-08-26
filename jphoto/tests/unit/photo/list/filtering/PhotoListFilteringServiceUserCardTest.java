package photo.list.filtering;

import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.photo.list.filtering.UserCardFilteringStrategy;
import org.junit.Test;

public class PhotoListFilteringServiceUserCardTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfPhotoNotWithinAnonymousPeriodTest() {

		final AbstractPhotoFilteringStrategy filteringStrategy = new UserCardFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminEvenIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;
		testData.accessor = SUPER_ADMIN_1;

		final AbstractPhotoFilteringStrategy filteringStrategy = new UserCardFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForUserCardOwnerEvenIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;
		testData.accessor = testData.photoAuthor;

		final AbstractPhotoFilteringStrategy filteringStrategy = new UserCardFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;

		final AbstractPhotoFilteringStrategy filteringStrategy = new UserCardFilteringStrategy( testData.accessor, getTestServices( testData ) );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
