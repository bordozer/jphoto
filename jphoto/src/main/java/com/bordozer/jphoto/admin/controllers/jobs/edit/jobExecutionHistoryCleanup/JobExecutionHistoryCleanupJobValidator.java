package com.bordozer.jphoto.admin.controllers.jobs.edit.jobExecutionHistoryCleanup;

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
public class JobExecutionHistoryCleanupJobValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return JobExecutionHistoryCleanupJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final JobExecutionHistoryCleanupJobModel model = (JobExecutionHistoryCleanupJobModel) target;

        validateDays(model.getDeleteEntriesOlderThenDays(), errors);
        validateStatuses(model.getJobExecutionStatusIdsToDelete(), errors);
    }

    private void validateDays(final String leaveActivityForDays, final Errors errors) {
        if (NumberUtils.convertToInt(leaveActivityForDays) < 0) {
            errors.rejectValue(JobExecutionHistoryCleanupJobModel.DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL,
                    translatorService.translate("$1 must be zero or a positive number", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("days")));
        }
    }

    private void validateStatuses(final List<String> jobExecutionStatusIdsToDelete, final Errors errors) {
        if (jobExecutionStatusIdsToDelete == null || jobExecutionStatusIdsToDelete.size() == 0) {
            errors.rejectValue(JobExecutionHistoryCleanupJobModel.DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL,
                    translatorService.translate("Select at least one $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("status")));
        }
    }
}
