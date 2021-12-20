package com.bordozer.jphoto.ui.controllers.users.photoAlbums.edit;

import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserPhotoAlbumEditDataValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserPhotoAlbumEditDataModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UserPhotoAlbumEditDataModel model = (UserPhotoAlbumEditDataModel) target;

        validateName(model, errors);
    }

    private void validateName(final UserPhotoAlbumEditDataModel model, final Errors errors) {
        if (StringUtils.isEmpty(model.getAlbumName())) {
            errors.rejectValue(UserPhotoAlbumEditDataModel.FORM_CONTROL_PHOTO_ALBUM_NAME
                    , translatorService.translate("$1 should not be empty", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Album name")));
        }
    }
}
