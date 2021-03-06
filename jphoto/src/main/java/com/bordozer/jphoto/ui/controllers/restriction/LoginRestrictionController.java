package com.bordozer.jphoto.ui.controllers.restriction;

import com.bordozer.jphoto.core.exceptions.AccessDeniedException;
import com.bordozer.jphoto.core.general.restriction.EntryRestriction;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsUserService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/restriction/{restrictionEntryId}/")
public class LoginRestrictionController {

    private static final String VIEW = "users/restriction/LoginRestriction";

    private static final String MODEL_NAME = "loginRestrictionModel";

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private BreadcrumbsUserService breadcrumbsUserService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @ModelAttribute(MODEL_NAME)
    private LoginRestrictionModel prepareModel(final @PathVariable("restrictionEntryId") String _restrictionEntryId) {

        final int restrictionEntryId = NumberUtils.convertToInt(_restrictionEntryId);
        if (restrictionEntryId == 0) {
            throw new AccessDeniedException("Wrong parameter"); // TODO:
        }

        return new LoginRestrictionModel(restrictionEntryId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showAvatar(final @ModelAttribute(MODEL_NAME) LoginRestrictionModel model) {

        // TODO: do not show this to any user
		/*if ( ! UserUtils.isCurrentUserLoggedUser() ) {
			return String.format( "redirect:/photos/" );
		}*/

        final EntryRestriction restriction = restrictionService.load(model.getRestrictionEntryId());
        model.setRestriction(restriction);

        model.setRestrictionDateFrom(dateUtilsService.formatDate(restriction.getRestrictionTimeFrom()));
        model.setRestrictionTimeFrom(dateUtilsService.formatTimeShort(restriction.getRestrictionTimeFrom()));

        model.setRestrictionDateTo(dateUtilsService.formatDate(restriction.getRestrictionTimeTo()));
        model.setRestrictionTimeTo(dateUtilsService.formatTimeShort(restriction.getRestrictionTimeTo()));

        model.setPageTitleData(breadcrumbsUserService.getUserLoginRestrictionBreadCrumbs(EnvironmentContext.getCurrentUser()));

        return VIEW;
    }
}
