package com.bordozer.jphoto.admin.controllers.jobs.edit.genreRank;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsersGenresRanksRecalculationValidator extends SavedJobValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return UsersGenresRanksRecalculationModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UsersGenresRanksRecalculationModel model = (UsersGenresRanksRecalculationModel) target;

        validateJobName(model, errors);
    }
}
