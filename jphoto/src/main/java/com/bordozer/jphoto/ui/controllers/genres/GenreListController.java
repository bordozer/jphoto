package com.bordozer.jphoto.ui.controllers.genres;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreviewWrapper;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.PhotoUIService;
import com.bordozer.jphoto.ui.services.breadcrumbs.CommonBreadcrumbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping(value = "genres")
public class GenreListController {

    private static final String MODEL_NAME = "genreListModel";
    private static final String VIEW = "genres/GenreList";

    @Autowired
    private GenreService genreService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoUIService photoUIService;

    @Autowired
    private UrlUtilsService urlUtilsService;

    @Autowired
    private CommonBreadcrumbsService commonBreadcrumbsService;

    @ModelAttribute(MODEL_NAME)
    public GenreListModel prepareModel() {

        final List<GenreListEntry> genreListEntries = newArrayList();

        final List<Genre> genres = genreService.loadAllSortedByNameForLanguage(getLanguage());
        for (final Genre genre : genres) {

            final GenreListEntry entry = new GenreListEntry(genre);
            entry.setGenreNameTranslated(translatorService.translateGenre(genre, getLanguage()));

            final int lastGenrePhotoId = photoService.getLastGenrePhotoId(genre.getId());
            if (lastGenrePhotoId == 0) {
                continue;
            }

            final Photo photo = photoService.load(lastGenrePhotoId);
            final PhotoPreviewWrapper previewWrapper = photoUIService.getPhotoPreviewWrapper(photo, EnvironmentContext.getCurrentUser());
            entry.setPhotoPreviewWrapper(previewWrapper);
            entry.setPhotosCount(photoService.getPhotosCountByGenre(genre.getId()));

            final User photoAuthor = userService.load(photo.getUserId());
            entry.setGenreIconTitle(translatorService.translate("Genre list: LAst uploaded photo. Author $1", getLanguage(), photoAuthor.getNameEscaped()));

            entry.setPhotosByGenreURL(urlUtilsService.getPhotosByGenreLink(genre.getId()));

            genreListEntries.add(entry);
        }

        return new GenreListModel(genreListEntries);
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showGenreList(final @ModelAttribute(MODEL_NAME) GenreListModel model) {

        model.setPageTitleData(commonBreadcrumbsService.genGenreListBreadcrumbs());

        return VIEW;
    }
}
