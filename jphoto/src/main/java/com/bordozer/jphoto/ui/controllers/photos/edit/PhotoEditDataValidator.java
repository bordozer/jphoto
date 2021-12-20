package com.bordozer.jphoto.ui.controllers.photos.edit;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import com.bordozer.jphoto.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PhotoEditDataValidator implements Validator {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotoEditDataModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoEditDataModel model = (PhotoEditDataModel) target;
        final String photoName = model.getPhotoName();

        validateUserIsLogged(errors);

        validatePhotoName(errors, photoName);

        validateGenreIsSelected(errors, model.getSelectedGenreId());
    }

    private void validateUserIsLogged(final Errors errors) {
        if (!UserUtils.isCurrentUserLoggedUser()) {
            errors.reject(translatorService.translate("Please, login", EnvironmentContext.getLanguage()), translatorService.translate("You are not logged in", EnvironmentContext.getLanguage()));
        }
    }

    private void validatePhotoName(final Errors errors, final String name) {

        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("photoName", translatorService.translate("$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name")));
            return;
        }

        final int photoNameMaxLength = configurationService.getInt(ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH);
        if (name.length() > photoNameMaxLength) {
            final String mess = translatorService.translate("$1 ($2) should be less then $3 symbols ( entered $2 symbols)", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Name"), String.valueOf(photoNameMaxLength), name, String.valueOf(name.length()));
            errors.rejectValue("photoName", mess);
        }
    }

    private void validateGenreIsSelected(final Errors errors, final int genreId) {
        if (genreId == 0) {
            errors.rejectValue("selectedGenreId", translatorService.translate("Select $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Genre")));
        }
    }
}
