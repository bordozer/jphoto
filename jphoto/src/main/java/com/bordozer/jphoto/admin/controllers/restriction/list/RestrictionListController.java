package com.bordozer.jphoto.admin.controllers.restriction.list;

import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/admin/restrictions")
@Controller
public class RestrictionListController {

    private static final String MODEL_NAME = "restrictionListModel";

    private static final String VIEW = "admin/restriction/list/RestrictionList";

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @ModelAttribute(MODEL_NAME)
    public RestrictionListModel prepareModel() {
        return new RestrictionListModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showRestrictions(final @ModelAttribute(MODEL_NAME) RestrictionListModel model) {

        model.setPageTitleData(breadcrumbsAdminService.getRestrictionListBreadcrumbs());

        return VIEW;
    }
}
