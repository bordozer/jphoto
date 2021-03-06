package com.bordozer.jphoto.ui.controllers.users.team.edit;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;

@SessionAttributes(UserTeamMemberEditDataController.MODEL_NAME)
@Controller
@RequestMapping("members/{userId}/team")
public class UserTeamMemberEditDataController {

    public static final String MODEL_NAME = "userTeamMemberEditDataModel";
    private static final String VIEW = "users/team/edit/UserTeamMemberEditData";

    @Autowired
    private UserTeamMemberEditDataValidator userTeamMemberEditDataValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private UrlUtilsServiceImpl urlUtilsService;

    @InitBinder
    private void initBinder(final WebDataBinder binder) {
        binder.setValidator(userTeamMemberEditDataValidator);
    }

    @ModelAttribute(MODEL_NAME)
    public UserTeamMemberEditDataModel prepareModel() {
        return new UserTeamMemberEditDataModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/")
    public String userTeamMemberNew(final @PathVariable("userId") String _userId, final @ModelAttribute(MODEL_NAME) UserTeamMemberEditDataModel model) {

        assertAccess(_userId);

        final int userId = NumberUtils.convertToInt(_userId);

        model.clear();

        final User user = userService.load(userId);

        model.setUser(user);

        model.setPageTitleData(breadcrumbsUserService.getUserTeamMemberNewBreadcrumbs(model.getUser()));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{teamMemberId}/edit/")
    public String userTeamMemberEdit(final @PathVariable("userId") String _userId, final @PathVariable("teamMemberId") int teamMemberId, final @ModelAttribute(MODEL_NAME) UserTeamMemberEditDataModel model) {

        assertAccess(_userId);

        final int userId = NumberUtils.convertToInt(_userId);

        model.clear();

        final User user = userService.load(userId);

        model.setUser(user);

        final UserTeamMember userTeamMember = userTeamService.load(teamMemberId);

        model.setUserTeamMemberId(userTeamMember.getId());
        model.setTeamMemberName(userTeamMember.getName());

        final User modelUser = userTeamMember.getTeamMemberUser();
        if (modelUser != null) {
            model.setTeamMemberUser(modelUser);
            model.setTeamMemberUserId(String.valueOf(modelUser.getId()));
        }
        model.setTeamMemberType(userTeamMember.getTeamMemberType());

        model.setPageTitleData(breadcrumbsUserService.getUserTeamMemberEditBreadcrumbs(userTeamMember));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public String userTeamMemberSave(@Valid final @ModelAttribute(MODEL_NAME) UserTeamMemberEditDataModel model, final BindingResult result) {

        final User user = model.getUser();

        assertAccess(String.valueOf(user.getId()));

        model.setBindingResult(result);

        if (result.hasErrors()) {
            return VIEW;
        }

        final UserTeamMember userTeamMember = initUserTeamMemberFromBean(model);

        if (!userTeamService.save(userTeamMember)) {
            return VIEW;
        }

        return String.format("redirect:%s", urlUtilsService.getUserTeamMembersLink(user.getId()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{teamMemberId}/delete/")
    public String userTeamMemberDelete(final @PathVariable("userId") String _userId, final @PathVariable("teamMemberId") int teamMemberId) {

        assertAccess(_userId);

        final int userId = NumberUtils.convertToInt(_userId);

        final String redirect = String.format("redirect:%s", urlUtilsService.getUserTeamMembersLink(userId));

        if (userTeamService.getTeamMemberPhotosQty(teamMemberId) > 0) {
            return redirect;
        }

        userTeamService.delete(teamMemberId);

        return redirect;
    }

    private UserTeamMember initUserTeamMemberFromBean(final UserTeamMemberEditDataModel model) {
        final UserTeamMember userTeamMember = new UserTeamMember();

        userTeamMember.setId(model.getUserTeamMemberId());
        userTeamMember.setUser(model.getUser());
        userTeamMember.setName(model.getTeamMemberName());

        final int modelUserId = NumberUtils.convertToInt(model.getTeamMemberUserId());
        if (modelUserId > 0) {
            userTeamMember.setTeamMemberUser(userService.load(modelUserId));
        }
        userTeamMember.setTeamMemberType(model.getTeamMemberType());

        return userTeamMember;
    }

    private void assertAccess(final String _userId) {
        securityService.assertUserExists(_userId);

        final int userId = NumberUtils.convertToInt(_userId);
        securityService.assertUserEqualsToCurrentUser(userId);
    }
}
