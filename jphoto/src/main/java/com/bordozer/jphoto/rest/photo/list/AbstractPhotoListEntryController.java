package com.bordozer.jphoto.rest.photo.list;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.security.SecurityUIService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class AbstractPhotoListEntryController {

    @Autowired
    protected PhotoService photoService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected GenreService genreService;

    @Autowired
    protected EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    protected UrlUtilsService urlUtilsService;

    @Autowired
    protected DateUtilsService dateUtilsService;

    @Autowired
    protected TranslatorService translatorService;

    @Autowired
    protected UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @Autowired
    protected SecurityUIService securityUIService;

    @Autowired
    protected SecurityService securityService;

    protected String getPhotoUploadDate(final Photo photo, final Language language) {
        final Date uploadTime = photo.getUploadTime();

        final String shownTime = dateUtilsService.isItToday(uploadTime) ? dateUtilsService.formatTimeShort(uploadTime) : dateUtilsService.formatDateTimeShort(uploadTime);

        return String.format("<a href='%s' title='%s'>%s</a>"
                , urlUtilsService.getPhotosUploadedOnDateUrl(uploadTime)
                , translatorService.translate("Photo preview: show all photos uploaded at $1", language, dateUtilsService.formatDate(uploadTime))
                , shownTime
        );
    }

    protected String getPhotoCategory(final int genreId, Language language) {
        final Genre genre = genreService.load(genreId);
        return entityLinkUtilsService.getPhotosByGenreLink(genre, language);
    }

    protected Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }

    protected String getPhotoPreview(final Photo photo, final User accessor, final Language language, final String photoPreviewUrl) {

        if (securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent(photo, accessor)) {
            return String.format("<a href='%s' title='%s'><img src='%s/nude_content.jpg' class='photo-preview-image block-border'/></a>"
                    , urlUtilsService.getPhotoCardLink(photo.getId())
                    , String.format("%s ( %s )", photo.getNameEscaped(), translatorService.translate("Photo preview: Nude content", language))
                    , urlUtilsService.getSiteImagesPath()
            );
        }

        return String.format("<a href='%s' title='%s'><img src='%s' class='photo-preview-image block-border'/></a>"
                , urlUtilsService.getPhotoCardLink(photo.getId())
                , photo.getNameEscaped()
                , photoPreviewUrl
        );
    }
}
