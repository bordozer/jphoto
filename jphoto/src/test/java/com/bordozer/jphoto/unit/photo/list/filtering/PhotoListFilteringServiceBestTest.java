package photo.list.filtering;

import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import com.bordozer.jphoto.core.services.photo.list.filtering.BestFilteringStrategy;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PhotoListFilteringServiceBestTest extends photo.list.filtering.AbstractPhotoListFilteringServiceTest_ {

    @Test
    public void photoShouldBeVisibleForUsualUserIfThePhotoIsNotRestrictedTest() {

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertPhotoIsShown(filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }

    @Test
    public void photoShouldBeVisibleForAdminIfThePhotoIsRestrictedTest() {

        testData.accessor = SUPER_ADMIN_1;
        testData.isRestricted = true;

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertPhotoIsShown(filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }

    @Test
    public void photoShouldNotBeVisibleForUsualUserIfThePhotoIsRestrictedTest() {

        testData.isRestricted = true;

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertEquals("Assertion fails", true, filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }

    @Test
    public void photoShouldNotBeVisibleForPhotoAuthorIfThePhotoIsRestrictedTest() {

        testData.accessor = testData.photoAuthor;
        testData.isRestricted = true;

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertPhotoIsHidden(filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }

    @Test
    public void photoShouldNotBeVisibleIfPhotoAuthorIsInInvisibilityListTest() {

        testData.isPhotoAuthorInInvisibilityList = true;

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertPhotoIsHidden(filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }

    @Test
    public void photoShouldBeVisibleForAdminsEvenIfPhotoAuthorIsInInvisibilityListTest() {

        testData.accessor = SUPER_ADMIN_1;
        testData.isPhotoAuthorInInvisibilityList = true;

        final AbstractPhotoFilteringStrategy filteringStrategy = new BestFilteringStrategy(testData.accessor, getTestServices(testData));

        assertPhotoIsShown(filteringStrategy.isPhotoHidden(testData.photo.getId(), testData.currentTime));
    }
}
