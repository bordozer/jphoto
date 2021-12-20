package com.bordozer.jphoto.ui.controllers.users.password.change;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import com.bordozer.jphoto.ui.services.validation.DataRequirementService;
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

import javax.validation.Valid;

@Controller
@RequestMapping("members/{userId}/password")
public class ChangeUserPasswordController {

    private static final String MODEL_NAME = "changeUserPasswordModel";
    private static final String VIEW = "users/password/change/ChangeUserPassword";
    private static final String VIEW_DONE = "users/password/change/ChangeUserPasswordDone";

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ChangeUserPasswordValidator changeUserPasswordValidator;

    @Autowired
    private DataRequirementService dataRequirementService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(changeUserPasswordValidator);
    }

    @ModelAttribute(MODEL_NAME)
    public ChangeUserPasswordModel prepareModel(final @PathVariable("userId") String _userId) {

        final int userId = NumberUtils.convertToInt(_userId);

        final User user = userService.load(userId);

        securityService.assertUserCanEditUserData(EnvironmentContext.getCurrentUser(), user);

        final ChangeUserPasswordModel model = new ChangeUserPasswordModel(user);

        model.setPageTitleData(breadcrumbsUserService.getChangeUserPasswordBreadcrumbs(user));
        model.setDataRequirementService(dataRequirementService);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showForm(final @ModelAttribute(MODEL_NAME) ChangeUserPasswordModel model) {
        model.clear();

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String changePassword(final @Valid @ModelAttribute(MODEL_NAME) ChangeUserPasswordModel model, final BindingResult result) {

        model.setBindingResult(result);

        if (result.hasErrors()) {
            return VIEW;
        }

        usersSecurityService.changeUserPassword(model.getUser(), model.getPassword());

        return VIEW_DONE;
    }
}
