package com.bordozer.jphoto.ui.controllers.users.password.change;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import com.bordozer.jphoto.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangeUserPasswordValidator implements Validator {

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return ChangeUserPasswordModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final ChangeUserPasswordModel model = (ChangeUserPasswordModel) target;

        validateOldPassword(model.getUser(), model.getOldPassword(), errors);

        if (!errors.hasErrors()) {
            validateNewPassword(model, errors);
        }
    }

    private void validateOldPassword(final User user, final String password, final Errors errors) {
        if (!usersSecurityService.isUserPasswordCorrect(user, password)) {
            errors.rejectValue(ChangeUserPasswordModel.FORM_CONTROL_OLD_PASSWORD, translatorService.translate("Incorrect $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Old password")));
        }
    }

    private void validateNewPassword(final ChangeUserPasswordModel model, final Errors errors) {
        usersSecurityService.validatePasswordCreation(model.getPassword(), model.getConfirmPassword(), errors);
    }
}
