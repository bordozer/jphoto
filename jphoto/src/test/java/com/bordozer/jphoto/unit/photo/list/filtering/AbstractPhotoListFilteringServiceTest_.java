package photo.list.filtering;

import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.easymock.EasyMock;
import org.junit.Before;

import static junit.framework.Assert.assertEquals;

public class AbstractPhotoListFilteringServiceTest_ extends AbstractTestCase {

    protected photo.list.filtering.TestData testData;

    @Before
    public void setup() {
        super.setup();

        testData = new photo.list.filtering.TestData(dateUtilsService);
    }

    protected Services getTestServices(final photo.list.filtering.TestData testData) {
        final ServicesImpl services = new ServicesImpl();

        services.setSecurityService(getSecurityService(testData));
        services.setRestrictionService(getRestrictionService(testData));
        services.setPhotoService(getPhotoService(testData));
        services.setFavoritesService(getFavoritesService(testData));

        return services;
    }

    private FavoritesService getFavoritesService(final photo.list.filtering.TestData testData) {
        final FavoritesService favoritesService = EasyMock.createMock(FavoritesService.class);

        EasyMock.expect(favoritesService.isUserInMembersInvisibilityListOfUser(testData.accessor.getId(), testData.photoAuthor.getId())).andReturn(testData.isPhotoAuthorInInvisibilityList).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(favoritesService);

        return favoritesService;
    }

    private SecurityService getSecurityService(final photo.list.filtering.TestData testData) {
        final SecurityService securityService = EasyMock.createMock(SecurityService.class);

        EasyMock.expect(securityService.isSuperAdminUser(testData.accessor)).andReturn(isSuperAdmin(testData.accessor)).anyTimes();
        EasyMock.expect(securityService.userOwnThePhoto(testData.accessor, testData.photo)).andReturn(testData.photo.getUserId() == testData.accessor.getId()).anyTimes();
        EasyMock.expect(securityService.isPhotoWithingAnonymousPeriod(testData.photo)).andReturn(testData.isPhotoWithingAnonymousPeriod).anyTimes();
        EasyMock.expect(securityService.isPhotoAuthorNameMustBeHidden(testData.photo, testData.accessor)).andReturn(testData.isPhotoAuthorHidden).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(securityService);

        return securityService;
    }

    private RestrictionService getRestrictionService(final photo.list.filtering.TestData testData) {
        final RestrictionService restrictionService = EasyMock.createMock(RestrictionService.class);

        EasyMock.expect(restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn(testData.photo.getId(), testData.currentTime)).andReturn(testData.isRestricted).anyTimes();
        EasyMock.expect(restrictionService.isPhotoShowingInTopBestRestrictedOn(testData.photo.getId(), testData.currentTime)).andReturn(testData.isRestricted).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(restrictionService);

        return restrictionService;
    }

    private PhotoService getPhotoService(final photo.list.filtering.TestData testData) {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.load(testData.photo.getId())).andReturn(testData.photo).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }

    protected void assertPhotoIsShown(final boolean photoIsHidden) {
        assertEquals("Photo is hidden but should not be", false, photoIsHidden);
    }

    protected void assertPhotoIsHidden(final boolean photoIsHidden) {
        assertEquals("Photo is shown but should be hidden", true, photoIsHidden);
    }
}
