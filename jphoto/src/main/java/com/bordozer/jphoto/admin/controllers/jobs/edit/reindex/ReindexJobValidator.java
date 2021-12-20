package com.bordozer.jphoto.admin.controllers.jobs.edit.reindex;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReindexJobValidator extends SavedJobValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return ReindexJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final ReindexJobModel model = (ReindexJobModel) target;

        validateJobName(model, errors);
    }
}
