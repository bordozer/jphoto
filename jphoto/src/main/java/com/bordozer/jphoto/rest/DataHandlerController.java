package com.bordozer.jphoto.rest;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.services.conversion.PreviewGenerationService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/rest/")
@Controller
public class DataHandlerController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PreviewGenerationService previewGenerationService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(method = RequestMethod.GET, value = "photos/{photoId}/nude-content/{isNudeContent}/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean setPhotoNudeContext(final @PathVariable("photoId") int photoId, final @PathVariable("isNudeContent") boolean isNudeContent) {

        final Photo photo = photoService.load(photoId);

        assertUserCanEditPhoto(photo);

        final Genre genre = genreService.load(photo.getGenreId());
        if (isNudeContent && !genre.isCanContainNudeContent()) {
            return false;
        }

        photo.setContainsNudeContent(isNudeContent);

        return photoService.save(photo);
    }

    @RequestMapping(method = RequestMethod.GET, value = "photos/{photoId}/preview/", produces = "application/json")
    @ResponseBody
    public boolean generatePreview(final @PathVariable("photoId") int photoId) throws IOException, InterruptedException {

        assertSuperAdminAccess();

        return previewGenerationService.generatePreviewSync(photoId) != null;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "photos/{photoId}/")
    @ResponseBody
    public boolean deletePhoto(final @PathVariable("photoId") int photoId) throws IOException, InterruptedException {

        final Photo photo = photoService.load(photoId);

        securityService.assertUserCanDeletePhoto(EnvironmentContext.getCurrentUser(), photo);

        return photoService.delete(photoId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "photos/{photoId}/move-to-genre/{genreId}/", produces = "application/json")
    @ResponseBody
    public boolean movePhotoToGenrePreview(final @PathVariable("photoId") int photoId, final @PathVariable("genreId") int genreId) {

        final Photo photo = photoService.load(photoId);

        assertUserCanEditPhoto(photo);

        if (!photoService.movePhotoToGenreWithNotification(photoId, genreId, EnvironmentContext.getCurrentUser())) {
            return false;
        }

        final Genre genre = genreService.load(genreId);
        photo.setContainsNudeContent(genre.isContainsNudeContent() || (photo.isContainsNudeContent() && genre.isCanContainNudeContent()));

        return photoService.save(photo);
    }

    private void assertSuperAdminAccess() {
        securityService.assertSuperAdminAccess(EnvironmentContext.getCurrentUser());
    }

    private void assertUserCanEditPhoto(final Photo photo) {
        securityService.assertUserCanEditPhoto(EnvironmentContext.getCurrentUser(), photo);
    }
}
