package com.bordozer.jphoto.ui.controllers.users.team.list;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping("members/{userId}/team/")
public class UserTeamMemberListController {

    private static final String MODEL_NAME = "userTeamMemberListModel";
    private static final String VIEW = "users/team/list/UserTeamMemberList";

    @Autowired
    private UserService userService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @ModelAttribute(MODEL_NAME)
    public UserTeamMemberListModel prepareModel(final @PathVariable("userId") String _userId) {

        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);
        final User user = userService.load(userId);

        final UserTeamMemberListModel model = new UserTeamMemberListModel();
        model.setUser(user);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showList(final @ModelAttribute(MODEL_NAME) UserTeamMemberListModel model) {

        final User user = model.getUser();

        final int userId = user.getId();

        final List<UserTeamMember> teamMembers = userTeamService.loadAllForEntry(userId);

        model.setUserTeamMembers(teamMembers);

        final Map<Integer, Integer> userTeamMemberPhotosQtyMap = newLinkedHashMap();
        for (final UserTeamMember teamMember : teamMembers) {
            final int teamMemberId = teamMember.getId();
            userTeamMemberPhotosQtyMap.put(teamMemberId, userTeamService.getTeamMemberPhotosQty(teamMemberId));
        }
        model.setUserTeamMemberPhotosQtyMap(userTeamMemberPhotosQtyMap);

        model.setPageTitleData(breadcrumbsUserService.getUserTeamMemberListBreadcrumbs(user));

        return VIEW;
    }
}
