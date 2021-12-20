package com.bordozer.jphoto.admin.controllers.jobs.edit.chain;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class JobChainJobValidator extends SavedJobValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return JobChainJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final JobChainJobModel model = (JobChainJobModel) target;

        validateJobName(model, errors);

        validateJobSelected(model, errors);
    }

    private void validateJobSelected(final JobChainJobModel model, final Errors errors) {
        final List<String> savedJobsIds = model.getSelectedSavedJobsIds();
        if (savedJobsIds == null || savedJobsIds.size() == 0) {
            errors.rejectValue(JobChainJobModel.SELECTED_SAVED_JOBS_IDS_FORM_CONTROL
                    , translatorService.translate("Select at least one $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Job")));
        }
    }
}
