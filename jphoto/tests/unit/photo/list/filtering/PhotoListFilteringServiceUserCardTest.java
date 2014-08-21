package photo.list.filtering;

import core.services.photo.list.PhotoListFilteringServiceImpl;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceUserCardTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleIfAuthorIsNotHiddenBecauseOfAnonymousPeriodTest() {

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.user, testData.accessor );

		assertEquals( "Assertion fails", false, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleIfAuthorIsHiddenBecauseOfAnonymousPeriodTest() {

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.accessor, testData.accessor );

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
