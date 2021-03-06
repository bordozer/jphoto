package com.bordozer.jphoto.admin.controllers.jobs.edit.action;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class PhotoActionGenerationValidator extends SavedJobValidator implements Validator {

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PhotoActionGenerationModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final PhotoActionGenerationModel model = (PhotoActionGenerationModel) target;

        validateTotalActions(model.getTotalActions(), errors);

        validatePhotosQty(model.getPhotosQty(), errors);

        validateDates(model.getDateFrom(), model.getDateTo(), errors);

        validateJobName(model, errors);
    }

    private void validateTotalActions(final String number, final Errors errors) {
        final String fieldName = "Total job steps";
        if (validateEmpty(number, errors, PhotoActionGenerationModel.TOTAL_ACTIONS_FORM_CONTROL, fieldName)) {
            validateNumber(number, errors, PhotoActionGenerationModel.TOTAL_ACTIONS_FORM_CONTROL, fieldName);
        }
    }

    private void validatePhotosQty(final String photoQty, final Errors errors) {
        if (StringUtils.isEmpty(photoQty)) {
            return;
        }
        validateNumber(photoQty, errors, PhotoActionGenerationModel.PHOTOS_QTY_FORM_CONTROL, "Photo qty");
    }

    private boolean validateEmpty(final String number, final Errors errors, String formControl, String fieldName) {
        if (StringUtils.isEmpty(number)) {
            errors.rejectValue(formControl, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName(fieldName)));
            return false;
        }
        return true;
    }

    private boolean validateNumber(final String number, final Errors errors, String formControl, String fieldName) {
        return !validatePositiveNumber(number, errors, formControl, fieldName);
    }

    private void validateDates(final String dateFrom, final String dateTo, final Errors errors) {
        if (StringUtils.isEmpty(dateFrom)) {
            errors.rejectValue(PhotoActionGenerationModel.DATE_FROM_FORM_CONTROL, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater from")));
            return;
        }

        if (StringUtils.isEmpty(dateTo)) {
            errors.rejectValue(PhotoActionGenerationModel.DATE_TO_FORM_CONTROL, translatorService.translate("Enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater to")));
            return;
        }

        final Date fromDate = dateUtilsService.parseDate(dateFrom);
        final Date toDate = dateUtilsService.parseDate(dateTo);

        if (toDate.getTime() < fromDate.getTime()) {
            errors.rejectValue(PhotoActionGenerationModel.DATE_TO_FORM_CONTROL, translatorService.translate("$1 should be more then $2", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Dater to"), FormatUtils.getFormattedFieldName("Dater from")));
        }
    }
}
