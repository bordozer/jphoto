package com.bordozer.jphoto.rest.photo.upload.category;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoUploadService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.rest.photo.upload.category.allowance.AbstractPhotoUploadAllowance;
import com.bordozer.jphoto.rest.photo.upload.category.allowance.PhotoUploadDescription;
import com.bordozer.jphoto.rest.photo.upload.category.allowance.UploadDescriptionFactory;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/users/{userId}/photos/{photoId}/edit/category/{categoryId}/")
public class PhotoCategoryHandlerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoUploadService photoUploadService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private Services services;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public PhotoCategoryHandlerDTO renderPhotoCategory(final @PathVariable("userId") int userId, final @PathVariable("photoId") int photoId, final @PathVariable("categoryId") int categoryId, final HttpServletRequest request) {

        final PhotoCategoryHandlerDTO dto = new PhotoCategoryHandlerDTO();

        dto.setPhotoId(photoId);
        dto.setUserId(userId);
        dto.setPhotoCategoryDTOs(getPhotoCategoryDTOs());

        if (categoryId > 0) {
            final PhotoUploadNudeContentDTO nudeContentDTO = getNudeContentDTO(categoryId, photoId);
            if (photoId > 0) {
                final Photo photo = photoService.load(photoId);
                dto.setPhotoContainsNude(photo.isContainsNudeContent());
            }
            dto.setNudeContentDTO(nudeContentDTO);
        }

        final Photo photo = photoService.load(photoId);
        if (photo != null) {
            dto.setSelectedCategoryId(photo.getGenreId());
        }

        if (photoId == 0) {
            final long fileSize = NumberUtils.convertToLong(request.getParameter("filesize"));
            dto.setFileSize(fileSize);
            dto.setPhotoUploadDescriptions(photoUploadAllowance(userId, categoryId, fileSize));
        }

        dto.setTextNudeContent(translatorService.translate("Photo uploading: Contains nude content", getLanguage()));
        dto.setTextNudeContentDescription(translatorService.translate("Photo uploading: Check this if the photo contains nude content", getLanguage()));

        return dto;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public PhotoCategoryHandlerDTO onChangePhotoCategory(@RequestBody final PhotoCategoryHandlerDTO dto) {

        if (dto.getPhotoId() == 0) {
            dto.setPhotoUploadDescriptions(photoUploadAllowance(dto.getUserId(), dto.getSelectedCategoryId(), dto.getFileSize()));
        }

        dto.setNudeContentDTO(getNudeContentDTO(dto.getSelectedCategoryId(), dto.getPhotoId()));

        return dto;
    }

    public PhotoUploadNudeContentDTO getNudeContentDTO(final int categoryId, final int photoId) {

        final Genre genre = genreService.load(categoryId);

        final PhotoUploadNudeContentDTO dto = new PhotoUploadNudeContentDTO();

        dto.setGenreCanContainsNude(genre.isCanContainNudeContent());
        dto.setGenreObviouslyContainsNude(genre.isContainsNudeContent());

        dto.setYesTranslated(translatorService.translate("Photo edit: Category always contains nude", getLanguage()));
        dto.setNoTranslated(translatorService.translate("Photo edit: Category can not contains nude", getLanguage()));

        return dto;
    }

    private List<PhotoCategoryDTO> getPhotoCategoryDTOs() {
        final Language language = getLanguage();

        final List<PhotoCategoryDTO> result = newArrayList();

        final List<Genre> genres = genreService.loadAllSortedByNameForLanguage(language);
        for (final Genre genre : genres) {
            final PhotoCategoryDTO wrapper = new PhotoCategoryDTO();
            wrapper.setCategoryId(genre.getId());
            wrapper.setName(translatorService.translateGenre(genre, language));

            result.add(wrapper);
        }

        return result;
    }

    public List<PhotoUploadDescription> photoUploadAllowance(final int userId, final int categoryId, final long fileSize) {

        final User user = userService.load(userId);
        final Genre genre = genreService.load(categoryId);

        final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance(user, EnvironmentContext.getCurrentUser(), getLanguage(), services);

        uploadAllowance.setUploadThisWeekPhotos(photoUploadService.getUploadedThisWeekPhotos(user));
        uploadAllowance.setGenre(genre);
        uploadAllowance.setFileSize(fileSize);

        return uploadAllowance.getUploadAllowance();
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
