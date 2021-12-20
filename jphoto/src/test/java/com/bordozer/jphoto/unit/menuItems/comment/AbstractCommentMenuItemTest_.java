package com.bordozer.jphoto.unit.menuItems.comment;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractCommentMenuItemTest_ extends AbstractTestCase {

    protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
    protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

    protected CommentMenuItemTestData testData;

    @Before
    public void setup() {
        super.setup();

        testData = new CommentMenuItemTestData();
    }

    protected ServicesImpl getServices(final User accessor) {
        final ServicesImpl services = new ServicesImpl();

        services.setPhotoCommentService(getPhotoCommentService());
        services.setPhotoService(getPhotoService());
        services.setSecurityService(getSecurityService(accessor));
        services.setUserService(getUserService());
        services.setTranslatorService(translatorService);

        return services;
    }

    private UserService getUserService() {
        final UserService userService = EasyMock.createMock(UserService.class);

        EasyMock.expect(userService.load(testData.getAccessor().getId())).andReturn(testData.getAccessor()).anyTimes();
        EasyMock.expect(userService.load(testData.getCommentAuthor().getId())).andReturn(testData.getCommentAuthor()).anyTimes();
        EasyMock.expect(userService.load(testData.getPhotoAuthor().getId())).andReturn(testData.getPhotoAuthor()).anyTimes();
        EasyMock.expect(userService.load(SUPER_ADMIN_1.getId())).andReturn(SUPER_ADMIN_1).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(userService);

        return userService;
    }

    private SecurityService getSecurityService(final User accessor) {
        final SecurityService securityService = EasyMock.createMock(SecurityService.class);

        EasyMock.expect(securityService.userOwnThePhotoComment(accessor, testData.getComment())).andReturn(testData.getComment().getCommentAuthor().getId() == accessor.getId()).anyTimes();

        EasyMock.expect(securityService.userOwnThePhoto(accessor, testData.getPhoto())).andReturn(testData.getPhoto().getUserId() == accessor.getId()).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(NOT_LOGGED_USER.getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(NOT_LOGGED_USER)).andReturn(false).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(testData.getAccessor().getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(testData.getAccessor())).andReturn(false).anyTimes();

        EasyMock.expect(securityService.isSuperAdminUser(testData.getCommentAuthor().getId())).andReturn(false).anyTimes();
        EasyMock.expect(securityService.isSuperAdminUser(testData.getCommentAuthor())).andReturn(false).anyTimes();

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

    protected PhotoService getPhotoService() {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.load(testData.getComment().getPhotoId())).andReturn(testData.getPhoto()).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }

    protected PhotoCommentService getPhotoCommentService() {
        final PhotoCommentService photoCommentService = EasyMock.createMock(PhotoCommentService.class);

        EasyMock.expect(photoCommentService.load(testData.getComment().getId())).andReturn(testData.getComment()).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoCommentService);

        return photoCommentService;
    }
}
