package com.bordozer.jphoto.admin.controllers.genres.list;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsServiceImpl;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminPhotoCategoriesService;
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
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@SessionAttributes({"genreListModel"})
@Controller
@RequestMapping("/admin/genres")
public class AdminGenreListController {

    public static final String VIEW = "/admin/genres/list/GenreList";

    @Autowired
    private GenreService genreService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BreadcrumbsAdminPhotoCategoriesService breadcrumbsAdminPhotoCategoriesService;
    @Autowired
    private TranslatorService translatorService;

    @Value("${app.adminPrefix}")
    private String adminPrefix;

    @ModelAttribute("genreListModel")
    public GenreListModel prepareModel() {
        return new GenreListModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showGenreList(final @ModelAttribute("genreListModel") GenreListModel model) {
        model.clear();

        model.setSystemMinMarksToBeInTheBestPhotoOfGenre(configurationService.getInt(ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO));

        final List<Genre> genres = genreService.loadAll();
        model.setGenreList(genres);

        final Map<Integer, Integer> photosByGenreMap = newHashMap();
        for (Genre genre : genres) {
            photosByGenreMap.put(genre.getId(), photoService.getPhotosCountByGenre(genre.getId()));
        }
        model.setPhotosByGenreMap(photosByGenreMap);

		/*final Map<Integer, List<PhotoVotingCategory>> genrePhotoVotingCategoriesMap = newHashMap();
		for ( final Genre genre : genres ) {
			final List<PhotoVotingCategory> list = newArrayList();

			genrePhotoVotingCategoriesMap.put( genre, list );
		}
		model.setGenrePhotoVotingCategoriesMap( genrePhotoVotingCategoriesMap );*/

        model.setPageTitleData(breadcrumbsAdminPhotoCategoriesService.getGenreListBreadcrumbs());

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{genreId}/delete/")
    public String deleteGenre(final @PathVariable("genreId") int genreId, final @ModelAttribute("genreListModel") GenreListModel model) {
        final boolean result = genreService.delete(genreId);

        if (!result) {
            BindingResult bindingResult = new BeanPropertyBindingResult(model, "genreListModel");
            bindingResult.reject(translatorService.translate("Registration error", EnvironmentContext.getLanguage()), translatorService.translate("Deletion error.", EnvironmentContext.getLanguage()));
            model.setBindingResult(bindingResult);
        }

        return String.format("redirect:/%s/%s/", adminPrefix, UrlUtilsServiceImpl.GENRES_URL);
    }
}
