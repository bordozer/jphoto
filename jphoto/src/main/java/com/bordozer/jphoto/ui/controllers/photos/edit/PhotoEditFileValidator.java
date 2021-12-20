package com.bordozer.jphoto.ui.controllers.photos.edit;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoEditFileValidator implements Validator {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotoEditDataModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoEditDataModel model = (PhotoEditDataModel) target;

        validateFile(errors, model);
    }

    private void validateFile(final Errors errors, final PhotoEditDataModel model) {

        final MultipartFile multipartFile = model.getPhotoFile();

        if (multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            errors.rejectValue("photoFile", translatorService.translate("Please, select a $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("photo file")));
            return;
        }

        final ConfigurationKey fileMaxSizeKey = model.getPhotoAuthor().getUserStatus() == UserStatus.CANDIDATE ? ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB : ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB;
        final long maxFileSizeKb = configurationService.getLong(fileMaxSizeKey);

        final int minFileWidth = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_MIN_WIDTH);
        final int minFileHeight = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_MIN_HEIGHT);
        final Dimension minDimension = new Dimension(minFileWidth, minFileHeight);

        final int maxFileWidth = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_MAX_WIDTH);
        final int maxFileHeight = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_MAX_HEIGHT);
        final Dimension maxDimension = new Dimension(maxFileWidth, maxFileHeight);

        imageFileUtilsService.validateUploadedFile(errors, multipartFile, maxFileSizeKb, maxDimension, minDimension, "photoFile", EnvironmentContext.getLanguage());
    }
}
