package com.bordozer.jphoto.admin.controllers.jobs.edit.userStatus;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserStatusRecalculationJobValidator extends SavedJobValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserStatusRecalculationJobModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UserStatusRecalculationJobModel model = (UserStatusRecalculationJobModel) target;

        validateJobName(model, errors);
    }
}
