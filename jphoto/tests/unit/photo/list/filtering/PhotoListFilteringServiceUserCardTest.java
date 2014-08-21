package photo.list.filtering;

import core.services.photo.PhotoListFilteringServiceImpl;
import org.junit.Test;
import ui.controllers.photos.list.factory.AbstractPhotoFilteringStrategy;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceUserCardTest extends AbstractPhotoListFilteringServiceTest_ {

	@Test
	public void photoShouldBeVisibleIfAuthorIsNotHiddenBecauseOfAnonymousPeriodTest() {

		testData.isAuthorIsHiddenBecauseOfAnonymousPeriod = false; // only this does matter

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.user, testData.accessor );

		assertEquals( "Assertion fails", false, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}

	@Test
	public void photoShouldNotBeVisibleIfAuthorIsHiddenBecauseOfAnonymousPeriodTest() {

		testData.isAuthorIsHiddenBecauseOfAnonymousPeriod = true; // only this does matter

		final PhotoListFilteringServiceImpl filteringService = getPhotoListFilteringService( testData );
		final AbstractPhotoFilteringStrategy filteringStrategy = filteringService.userCardFilteringStrategy( testData.user, testData.accessor );

		assertEquals( "Assertion fails", true, filteringStrategy.isPhotoHidden( testData.photo.getId(), testData.currentTime ) );
	}
}
