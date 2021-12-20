package com.bordozer.jphoto.admin.controllers.votingCategories.edit;

import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import com.bordozer.jphoto.ui.services.validation.DataRequirementService;
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

@SessionAttributes({"votingCategoryEditDataModel"})
@Controller
@RequestMapping("/admin/votingcategories")
public class VotingCategoryEditDataController {

    public static final String VIEW = "admin/votingCategories/edit/VotingCategoryEditData";

    @Autowired
    private VotingCategoryService votingCategoryService;
    @Autowired
    private VotingCategoryEditDataValidator votingCategoryEditDataValidator;
    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;
    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private UrlUtilsService urlUtilsService;
    @Autowired
    private DataRequirementService dataRequirementService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(votingCategoryEditDataValidator);
    }

    @ModelAttribute("votingCategoryEditDataModel")
    public VotingCategoryEditDataModel prepareModel() {
        final VotingCategoryEditDataModel model = new VotingCategoryEditDataModel();

        model.setDataRequirementService(dataRequirementService);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/")
    public String newVotingCategory(@ModelAttribute("votingCategoryEditDataModel") VotingCategoryEditDataModel model) {
        model.clear();

        model.setNew(true);
        model.setPageTitleData(breadcrumbsAdminService.getVotingCategoryNewBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{categoryId}/edit/")
    public String editVotingCategory(final @PathVariable("categoryId") int categoryId, final @ModelAttribute("votingCategoryEditDataModel") VotingCategoryEditDataModel model) {
        model.clear();

        final PhotoVotingCategory photoVotingCategory = votingCategoryService.load(categoryId);

        initModelFromObject(model, photoVotingCategory);

        model.setPageTitleData(breadcrumbsAdminService.getVotingCategoryEditDataBreadcrumbs(photoVotingCategory));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public String saveVotingCategory(@Valid final @ModelAttribute("votingCategoryEditDataModel") VotingCategoryEditDataModel model, final BindingResult result) {
        model.setBindingResult(result);

        if (result.hasErrors()) {
            return VIEW;
        }

        final PhotoVotingCategory photoVotingCategory = getObjectFromModel(model);

        if (!votingCategoryService.save(photoVotingCategory)) {
            final Language language = EnvironmentContext.getLanguage();
            result.reject(translatorService.translate("Saving data error", language), translatorService.translate("Error saving data to DB", language));
            return VIEW;
        }

        return String.format("redirect:%s/%s/", urlUtilsService.getBaseAdminURL(), UrlUtilsServiceImpl.VOTING_CATEGORIES_URL);
    }

    private void initModelFromObject(final VotingCategoryEditDataModel model, final PhotoVotingCategory photoVotingCategory) {
        model.setVotingCategoryId(photoVotingCategory.getId());
        model.setVotingCategoryName(photoVotingCategory.getName());
        model.setVotingCategoryDescription(photoVotingCategory.getDescription());
    }

    private PhotoVotingCategory getObjectFromModel(final VotingCategoryEditDataModel model) {
        final PhotoVotingCategory photoVotingCategory = new PhotoVotingCategory();
        photoVotingCategory.setId(model.getVotingCategoryId());
        photoVotingCategory.setName(model.getVotingCategoryName());
        photoVotingCategory.setDescription(model.getVotingCategoryDescription());
        return photoVotingCategory;
    }

}
