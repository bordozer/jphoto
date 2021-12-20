package com.bordozer.jphoto.admin.controllers.reports.users;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping("/admin/reports/users")
public class UserReportController {

    public static final String MODEL_NAME = "userReportModel";

    private static final String VIEW = "admin/reports/users/UsersReport";

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @Autowired
    private UserService userService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @ModelAttribute(MODEL_NAME)
    public UserReportModel prepareModel() {
        final UserReportModel model = new UserReportModel();

        model.setPageTitleData(breadcrumbsAdminService.getUsersReportBreadcrumbs());

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String controlPanel(final @ModelAttribute(MODEL_NAME) UserReportModel model) {
        final List<User> users = userService.loadAll();

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(final User o1, final User o2) {
                return o1.getRegistrationTime().compareTo(o2.getRegistrationTime());
            }
        });

        final User firstUser = users.get(0);
        final Date firstRegistrationTime = firstUser.getRegistrationTime();
        final Date firstRegistrationDate = dateUtilsService.getFirstSecondOfDay(firstRegistrationTime);
        final int daysSinceFirstUserRegistrations = dateUtilsService.getDifferenceInDays(firstRegistrationDate, dateUtilsService.getFirstSecondOfToday());

        final Map<Date, UserRegistrationData> registrationsMap = newLinkedHashMap();

        for (int i = 0; i <= daysSinceFirstUserRegistrations; i++) {
            registrationsMap.put(dateUtilsService.getDatesOffset(firstRegistrationDate, i), new UserRegistrationData());
        }

        for (final User user : users) {
            final Date registrationDate = dateUtilsService.getFirstSecondOfDay(user.getRegistrationTime());

            final UserRegistrationData userRegistrationData = registrationsMap.get(registrationDate);
            if (userRegistrationData != null) {
                userRegistrationData.increaseUsersCount();
            }
        }

        model.setRegistrationsMap(registrationsMap);

        return VIEW;
    }
}
