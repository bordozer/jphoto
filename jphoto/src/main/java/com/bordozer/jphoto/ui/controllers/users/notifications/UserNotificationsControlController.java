package com.bordozer.jphoto.ui.controllers.users.notifications;

import com.bordozer.jphoto.core.general.user.EmailNotificationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Controller
@RequestMapping("members/{userId}/notifications")
public class UserNotificationsControlController {

    private static final String MODEL_NAME = "userNotificationsControlModel";
    private static final String VIEW = "users/notifications/UserNotificationsControl";

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private FavoritesService favoritesService;

    @ModelAttribute(MODEL_NAME)
    public UserNotificationsControlModel prepareModel(final @PathVariable("userId") String _userId) {
        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);
        final User user = userService.load(userId);

        securityService.assertUserEqualsToCurrentUser(user);

        final UserNotificationsControlModel model = new UserNotificationsControlModel();

        model.setUser(user);

        final Set<String> notificationOptionIds = newHashSet();
        for (final EmailNotificationType emailNotificationType : user.getEmailNotificationTypes()) {
            notificationOptionIds.add(String.valueOf(emailNotificationType.getId()));
        }
        model.setEmailNotificationTypeIds(notificationOptionIds);

        model.setUsersQtyWhoNewPhotoUserIsTracking(favoritesService.getUsersQtyWhoNewPhotoUserIsTracking(user));
        model.setPhotoQtyWhichCommentsUserIsTracking(favoritesService.getPhotoQtyWhichCommentsUserIsTracking(user));
        model.setFriendsQty(favoritesService.getFriendsQty(user));

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showList(final @ModelAttribute(MODEL_NAME) UserNotificationsControlModel model) {

        final User user = model.getUser();

        model.setPageTitleData(breadcrumbsUserService.getUserNotificationsControlBreadcrumbs(user));

        return VIEW;
    }
}
