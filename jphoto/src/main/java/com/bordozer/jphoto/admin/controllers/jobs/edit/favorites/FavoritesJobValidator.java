package com.bordozer.jphoto.admin.controllers.jobs.edit.favorites;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FavoritesJobValidator extends SavedJobValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return FavoritesJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final FavoritesJobModel model = (FavoritesJobModel) target;

        validateJobName(model, errors);

        validateActionQty(model, errors);

        validateFavoriteEntries(model, errors);
    }

    private void validateActionQty(final FavoritesJobModel model, final Errors errors) {
        validatePositiveNumber(model.getActionsQty(), errors, FavoritesJobModel.ACTIONS_QTY_FORM_CONTROL, "Total job steps");
    }

    private void validateFavoriteEntries(final FavoritesJobModel model, final Errors errors) {
        if (model.getFavoriteEntriesIds() == null || model.getFavoriteEntriesIds().size() == 0) {
            final String errorCode = translatorService.translate("Select at least one checkbox of $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Favorite entries"));
            errors.rejectValue(FavoritesJobModel.FAVORITE_ENTRIES_IDS_FORM_CONTROL, errorCode);
        }
    }
}
