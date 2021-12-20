package com.bordozer.jphoto.admin.controllers.jobs.edit.archiving;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ArchivingJobJobValidator extends SavedJobValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ArchivingJobJobModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final ArchivingJobJobModel model = (ArchivingJobJobModel) target;

        validateJobName(model, errors);
    }
}
