package com.bordozer.jphoto.admin.controllers.votingCategories.list;

import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@SessionAttributes({"votingCategoryListModel"})
@Controller
@RequestMapping("/admin/votingcategories")
public class VotingCategoryListController {

    public static final String VIEW = "/admin/votingCategories/list/VotingCategoryList";

    @Autowired
    private VotingCategoryService votingCategoryService;
    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;
    @Autowired
    private TranslatorService translatorService;

    @Value("${app.adminPrefix}")
    private String adminPrefix;

    @ModelAttribute("votingCategoryListModel")
    public VotingCategoryListModel prepareModel() {
        return new VotingCategoryListModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showVotingCategoryList(final @ModelAttribute("votingCategoryListModel") VotingCategoryListModel model) {
        model.clear();

        final List<PhotoVotingCategory> list = votingCategoryService.loadAll();
        model.setPhotoVotingCategories(list);
        model.setPageTitleData(breadcrumbsAdminService.getVotingCategoryListBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{votingCategoryId}/delete/")
    public String deleteVotingCategory(@PathVariable("votingCategoryId") int votingCategoryId, @ModelAttribute("votingCategoryListModel") VotingCategoryListModel model) {
        final boolean result = votingCategoryService.delete(votingCategoryId);

        if (!result) {
            BindingResult bindingResult = new BeanPropertyBindingResult(model, "votingCategoryListModel");
            bindingResult.reject(translatorService.translate("Registration error", EnvironmentContext.getLanguage()), translatorService.translate("Deletion error.", EnvironmentContext.getLanguage()));
            model.setBindingResult(bindingResult);
        }

        return String.format("redirect:/%s/%s/", adminPrefix, UrlUtilsServiceImpl.VOTING_CATEGORIES_URL);
    }
}
