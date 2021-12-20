package com.bordozer.jphoto.rest.photo.list.bigPreview;

import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.rest.photo.list.AbstractPhotoListEntryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/rest/photos/{photoId}")
@Controller
public class PhotoListBigPreviewEntryController extends AbstractPhotoListEntryController {

    private static final int PHOTO_MAX_WIDTH = 1000;
    private static final int PHOTO_MAX_HEIGHT = 500;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    @RequestMapping(method = RequestMethod.GET, value = "/big-preview/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public PhotoBigEntryDTO photoListEntry(final @PathVariable("photoId") int photoId) {

        final Photo photo = photoService.load(photoId);
        final Language language = getLanguage();

        final PhotoBigEntryDTO dto = new PhotoBigEntryDTO();

        dto.setPhotoName(photo.getName()); // TODO: escaping!
        dto.setPhotoImageUrl(userPhotoFilePathUtilsService.getPhotoImageUrl(photo));
        dto.setPhotoUploadDate(getPhotoUploadDate(photo, language));
        dto.setPhotoCategory(getPhotoCategory(photo.getGenreId(), language));
        dto.setPhotoLink(entityLinkUtilsService.getPhotoCardLink(photo, language));
        dto.setPhotoCardLink(urlUtilsService.getPhotoCardLink(photo.getId()));
        dto.setPhotoDescription(photo.getDescription()); // TODO: escaping!

        final User photoAuthor = userService.load(photo.getUserId());
        dto.setPhotoAuthorLink(entityLinkUtilsService.getUserCardLink(photoAuthor, language));

        final Dimension dimension = imageFileUtilsService.resizePhotoImage(photo.getImageDimension(), new Dimension(PHOTO_MAX_WIDTH, PHOTO_MAX_HEIGHT));
        dto.setPhotoImageWidth(dimension.getWidth());
        dto.setPhotoImageHeight(dimension.getHeight());

        dto.setMinContainerWidth(PHOTO_MAX_WIDTH + 20);

        return dto;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/big-preview/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deletePhoto(final @PathVariable("photoId") int photoId) {
        photoService.delete(photoId);
    }
}
