package com.bordozer.jphoto.admin.controllers.genres.edit;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminPhotoCategoriesService;
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
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes({"genreEditDataModel"})
@Controller
@RequestMapping("/admin/genres")
public class GenreEditDataController {

    public static final String VIEW = "admin/genres/edit/GenreEditData";

    public static final String MODEL_NAME = "genreEditDataModel";

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreEditDataValidator genreEditDataValidator;

    @Autowired
    private VotingCategoryService votingCategoryService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private BreadcrumbsAdminPhotoCategoriesService breadcrumbsAdminPhotoCategoriesService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private DataRequirementService dataRequirementService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(genreEditDataValidator);
    }

    @ModelAttribute(MODEL_NAME)
    public GenreEditDataModel prepareModel() {
        final GenreEditDataModel model = new GenreEditDataModel();

        model.setDataRequirementService(dataRequirementService);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/")
    public String newGenre(final @ModelAttribute(MODEL_NAME) GenreEditDataModel model) {
        model.clear();

        model.setNew(true);
        model.setPageTitleData(breadcrumbsAdminPhotoCategoriesService.getGenreNewBreadcrumbs());
        model.setPhotoVotingCategories(votingCategoryService.loadAll());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{genreId}/edit/")
    public String editGenre(final @PathVariable("genreId") int genreId, final @ModelAttribute(MODEL_NAME) GenreEditDataModel model) {
        model.clear();

        final Genre genre = new Genre(genreService.load(genreId));
        model.setGenreId(genre.getId());
        model.setGenreName(genre.getName());
        model.setDescription(genre.getDescription());
        model.setCanContainNudeContent(genre.isCanContainNudeContent());
        model.setContainsNudeContent(genre.isContainsNudeContent());
        model.setMinMarksForBest(genre.getMinMarksForBest());
        model.setPhotoVotingCategories(votingCategoryService.loadAll());

        final List<String> allowedVotingCategoryIDs = newArrayList();
        for (PhotoVotingCategory photoVotingCategory : genre.getPhotoVotingCategories()) {
            allowedVotingCategoryIDs.add(String.valueOf(photoVotingCategory.getId()));
        }
        model.setAllowedVotingCategoryIDs(allowedVotingCategoryIDs);

        model.setPageTitleData(breadcrumbsAdminPhotoCategoriesService.getGenreDataEditBreadcrumbs(genre));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public String saveGenre(@Valid final @ModelAttribute(MODEL_NAME) GenreEditDataModel model, final BindingResult result) {
        model.setBindingResult(result);

        if (result.hasErrors()) {
            return VIEW;
        }

        final Genre genre = new Genre();
        genre.setId(model.getGenreId());
        genre.setName(model.getGenreName());
        genre.setDescription(model.getDescription());
        genre.setCanContainNudeContent(model.isCanContainNudeContent());
        genre.setContainsNudeContent(model.isContainsNudeContent());
        genre.setMinMarksForBest(model.getMinMarksForBest());

        genre.setPhotoVotingCategories(model.getAllowedVotingCategories());

        if (!genreService.save(genre)) {
            result.reject(translatorService.translate("Saving data error", EnvironmentContext.getLanguage()), translatorService.translate("Error saving data to DB", EnvironmentContext.getLanguage()));
            return VIEW;
        }

        return String.format("redirect:%s/%s/", urlUtilsService.getBaseAdminURL(), UrlUtilsServiceImpl.GENRES_URL);
    }

}
