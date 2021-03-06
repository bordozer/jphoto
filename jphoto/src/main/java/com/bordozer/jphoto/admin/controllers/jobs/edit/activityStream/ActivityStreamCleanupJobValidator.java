package com.bordozer.jphoto.admin.controllers.jobs.edit.activityStream;

import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ActivityStreamCleanupJobValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return ActivityStreamCleanupJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final ActivityStreamCleanupJobModel model = (ActivityStreamCleanupJobModel) target;

        validateLeaveActivityForDays(model.getLeaveActivityForDays(), errors);
        validateActivityTypes(model.getActivityStreamTypeIdsToDelete(), errors);
    }

    private void validateLeaveActivityForDays(final String leaveActivityForDays, final Errors errors) {
        if (NumberUtils.convertToInt(leaveActivityForDays) < 0) {
            errors.rejectValue(ActivityStreamCleanupJobModel.LEAVE_ACTIVITY_FOR_DAYS_CONTROL,
                    translatorService.translate("Please, enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("days")));
        }
    }

    private void validateActivityTypes(final List<String> activityStreamTypeIdsToDelete, final Errors errors) {
        if (activityStreamTypeIdsToDelete == null || activityStreamTypeIdsToDelete.size() == 0) {
            errors.rejectValue(ActivityStreamCleanupJobModel.ACTIVITY_STREAM_TYPE_IDS_TO_DELETE_CONTROL,
                    translatorService.translate("Select at least one $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("activity type")));
        }
    }
}
