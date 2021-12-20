package com.bordozer.jphoto.ui.controllers.photos.groupoperations;

import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class PhotoGroupOperationValidator implements Validator {

    public static final int NO_GENRE_SELECTED = -1;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotoGroupOperationModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoGroupOperationModel model = (PhotoGroupOperationModel) target;

        validateGroupOperationSelected(model, errors);

        validateAtLeastOnePhotoSelected(model, errors);

        validateMoveToGenreSelected(model, errors);
    }

    void validateGroupOperationSelected(final PhotoGroupOperationModel model, final Errors errors) {
        final String groupOperationId = model.getPhotoGroupOperationId();

        if (StringUtils.isEmpty(groupOperationId)) {
            errors.reject(translatorService.translate("Validation error", getLanguage()), translatorService.translate("Please, select group operation", getLanguage()));
            return;
        }

        final PhotoGroupOperationType groupOperationType = PhotoGroupOperationType.getById(Integer.parseInt(groupOperationId));
        if (groupOperationType == null) {
            errors.reject(translatorService.translate("Validation error", getLanguage()), translatorService.translate("Group operation is unknown", getLanguage()));
        }
    }

    void validateAtLeastOnePhotoSelected(final PhotoGroupOperationModel model, final Errors errors) {
        final List<String> selectedPhotoIds = model.getSelectedPhotoIds();
        if (selectedPhotoIds == null || selectedPhotoIds.size() == 0) {
            errors.reject(translatorService.translate("Validation error", getLanguage()), translatorService.translate("Please, select at least one photo", getLanguage()));
        }
    }

    private void validateMoveToGenreSelected(final PhotoGroupOperationModel model, final Errors errors) {
        final PhotoGroupOperationType groupOperationType = model.getPhotoGroupOperationType();

        if (groupOperationType == null) {
            return;
        }

        if (groupOperationType != PhotoGroupOperationType.MOVE_TO_GENRE) {
            return;
        }

        if (model.getMoveToGenreId() == NO_GENRE_SELECTED) {
            errors.rejectValue(PhotoGroupOperationModel.FORM_CONTROL_MOVE_TO_GENRE_ID, translatorService.translate("Select %s to move to.", getLanguage(), FormatUtils.getFormattedFieldName("genre")));
        }
    }

    private static Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
