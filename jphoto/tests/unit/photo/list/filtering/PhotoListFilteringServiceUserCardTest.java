package photo.list.filtering;

import core.services.photo.list.PhotoListFilteringServiceImpl;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import org.junit.Test;

public class PhotoListFilteringServiceUserCardTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleForUsualUserIfPhotoNotWithinAnonymousPeriodTest() {

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.user, testData.accessor );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForAdminEvenIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;
		testData.accessor = SUPER_ADMIN_1;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.accessor, testData.accessor );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldBeVisibleForUserCardOwnerEvenIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;
		testData.accessor = testData.photoAuthor;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.accessor, testData.accessor );

		assertPhotoIsShown( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleForUsualUserIfPhotoIsWithinAnonymousPeriodTest() {

		testData.isPhotoWithingAnonymousPeriod = true;

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.accessor, testData.accessor );

		assertPhotoIsHidden( filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
