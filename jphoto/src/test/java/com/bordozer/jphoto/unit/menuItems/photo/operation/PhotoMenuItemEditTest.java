package menuItems.photo.operation;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation.PhotoMenuItemEdit;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemEditTest extends AbstractPhotoMenuItemTest_ {

    @Test
    public void userCanNOTSeeMenuIfHeHasNoAccessToEditPhotoTest() {
        final User accessor = testData.getPhotoAuthor();
        final boolean hasAccessTo = false;

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemEdit(testData.getPhoto(), accessor, getServices(accessor, hasAccessTo)).hasAccessTo());
    }

    @Test
    public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {
        final User accessor = testData.getPhotoAuthor();
        final boolean hasAccessTo = true;

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemEdit(testData.getPhoto(), accessor, getServices(accessor, hasAccessTo)).hasAccessTo());
    }

    private ServicesImpl getServices(final User accessor, final boolean hasAccessTo) {
        final ServicesImpl services = getServices(accessor);

        services.setSecurityService(getSecurityService(accessor, hasAccessTo));

        return services;
    }

    private SecurityService getSecurityService(final User accessor, final boolean hasAccessTo) {
        final SecurityService securityService = EasyMock.createMock(SecurityService.class);

        EasyMock.expect(securityService.userCanEditPhoto(EasyMock.<User>anyObject(), EasyMock.<Photo>anyObject())).andReturn(hasAccessTo).anyTimes();

        EasyMock.expect(securityService.userOwnThePhoto(accessor, testData.getPhoto().getId())).andReturn(testData.getPhoto().getUserId() == accessor.getId()).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(NOT_LOGGED_USER.getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(NOT_LOGGED_USER)).andReturn(false).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(testData.getAccessor().getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(testData.getAccessor())).andReturn(false).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(testData.getPhotoAuthor().getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(testData.getPhotoAuthor())).andReturn(false).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(SUPER_ADMIN_1.getId())).andReturn(true).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(SUPER_ADMIN_1)).andReturn(true).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(SUPER_ADMIN_2.getId())).andReturn(true).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(SUPER_ADMIN_2)).andReturn(true).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(securityService);

        return securityService;
    }
}
