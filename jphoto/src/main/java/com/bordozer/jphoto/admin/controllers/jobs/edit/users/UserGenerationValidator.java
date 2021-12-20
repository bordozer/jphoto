package com.bordozer.jphoto.admin.controllers.jobs.edit.users;

import com.bordozer.jphoto.admin.controllers.jobs.edit.SavedJobValidator;
import com.bordozer.jphoto.admin.jobs.entries.UserGenerationJob;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.File;

@Component
public class UserGenerationValidator extends SavedJobValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserGenerationModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UserGenerationModel model = (UserGenerationModel) target;

        validateUserQty(model.getUserQtyLimit(), errors);

        validateJobName(model, errors);

        validateAvatarDir(model.getAvatarsDir(), errors);
    }

    private void validateUserQty(final String userQtyLimit, final Errors errors) {
        validateNonZeroPositiveNumber(userQtyLimit, errors, "User qty", UserGenerationModel.USER_QTY_FORM_CONTROL);
    }

    private void validateAvatarDir(final String avatarPath, final Errors errors) {
        if (StringUtils.isEmpty(avatarPath)) {
            return;
        }

        final File avatarDir = new File(avatarPath);
        if (!avatarDir.exists()) {
            errors.rejectValue(UserGenerationModel.AVATAR_DIR_FORM_CONTROL
                    , translatorService.translate("Folder $1 does not exist", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Avatar dir")));
            return;
        }

        final File maleAvatarDir = new File(avatarPath, UserGenerationJob.AVATAR_SUBDIR_MALE);
        if (!maleAvatarDir.exists()) {
            errors.rejectValue(UserGenerationModel.AVATAR_DIR_FORM_CONTROL
                    , translatorService.translate("$1 does not consist 'male' folder", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Avatar dir")));
            return;
        }

        final File femaleAvatarDir = new File(avatarPath, UserGenerationJob.AVATAR_SUBDIR_FEMALE);
        if (!femaleAvatarDir.exists()) {
            errors.rejectValue(UserGenerationModel.AVATAR_DIR_FORM_CONTROL
                    , translatorService.translate("$1 does not consist 'female' folder", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName("Avatar dir")));
            return;
        }
    }
}
