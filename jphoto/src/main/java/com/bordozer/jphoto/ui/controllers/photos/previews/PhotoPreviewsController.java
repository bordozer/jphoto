package com.bordozer.jphoto.ui.controllers.photos.previews;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoPreviewService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.PhotoUIService;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsPhotoService;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "photos/{photoId}/previews/")
public class PhotoPreviewsController {

    private static final String MODEL_NAME = "photoPreviewsModel";
    private static final String VIEW = "photos/previews/PreviewList";

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoUIService photoUIService;

    @Autowired
    private PhotoPreviewService photoPreviewService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BreadcrumbsPhotoService breadcrumbsPhotoService;

    @ModelAttribute(MODEL_NAME)
    public PhotoPreviewsModel prepareModel() {
        return new PhotoPreviewsModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String commentsToUser(final @PathVariable("photoId") String _photoId, final @ModelAttribute(MODEL_NAME) PhotoPreviewsModel model) {

        securityService.assertPhotoExists(_photoId);

        final int photoId = NumberUtils.convertToInt(_photoId);

        final Photo photo = photoService.load(photoId);

        model.setPhoto(photo);

        model.setPhotoPreviews(photoPreviewService.getPreviews(photoId));

        final User user = userService.load(photo.getUserId());
        final Genre genre = genreService.load(photo.getGenreId());

        model.setPhotoAuthor(user);
        model.setGenre(genre);

        model.setPhotoPreviewWrapper(photoUIService.getPhotoPreviewWrapper(photo, EnvironmentContext.getCurrentUser()));

        model.setPageTitleData(breadcrumbsPhotoService.getUserPhotoPreviewsBreadcrumbs(photo, EnvironmentContext.getCurrentUser()));

        return VIEW;
    }
}
