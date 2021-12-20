package com.bordozer.jphoto.ui.services.validation;

import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.users.edit.UserEditDataModel;
import com.bordozer.jphoto.utils.PasswordUtils;

public class UserRequirement {

    private TranslatorService translatorService;

    public UserRequirement(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public String getLoginRequirement(final int minLoginLength, final int maxLoginLength) {
        final StringBuilder builder = new StringBuilder();

        final String minLen = String.valueOf(minLoginLength);
        final String maxLen = String.valueOf(maxLoginLength);
        builder.append(translatorService.translate("UserRequirement: Your login should have length $1-$2, a-z, A-Z, 0-9 or _.", getLanguage(), minLen, maxLen));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: Your login will never be shown to another users.", getLanguage()));

        return builder.toString();
    }

    public String getPasswordRequirement(final boolean isClickablePasswordExamples) {
        final StringBuilder builder = new StringBuilder();
        builder.append(translatorService.translate("UserRequirement: Must contains one digit from 0-9", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: Must contains one lowercase characters", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: Must contains one uppercase characters", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(String.format("%s %s", translatorService.translate("UserRequirement: Must contains one special symbols from the list", getLanguage()), "'@#$%^&+='"));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: No whitespace allowed in the entire string", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        builder.append(translatorService.translate("UserRequirement: Randomly generated passwords (you can use one of them):", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        builder.append(String.format("%s: %s", translatorService.translate("UserRequirement: Minimal secure password example", getLanguage())
                , getClickablePassword(PasswordUtils.generatePassword(UserEditDataModel.MIN_PASSWORD_LENGTH), isClickablePasswordExamples)));
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        final int length = UserEditDataModel.MIN_PASSWORD_LENGTH + (UserEditDataModel.MAX_PASSWORD_LENGTH - UserEditDataModel.MIN_PASSWORD_LENGTH) / 2;
        builder.append(String.format("%s: %s", translatorService.translate("UserRequirement: Good password example", getLanguage())
                , getClickablePassword(PasswordUtils.generatePassword(length), isClickablePasswordExamples)));
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        builder.append(String.format("%s: %s", translatorService.translate("UserRequirement: Excellent password example", getLanguage())
                , getClickablePassword(PasswordUtils.generatePassword(UserEditDataModel.MAX_PASSWORD_LENGTH), isClickablePasswordExamples)));

        return builder.toString();
    }

    private Language getLanguage() {
        return EnvironmentContext.getCurrentUser().getLanguage();
    }

    private String getClickablePassword(final String password, final boolean isClickablePasswordExamples) {
        if (!isClickablePasswordExamples) {
            return password;
        }

        return String.format("<a href='#' onclick=\"$( '#%1$s' ).val( '%3$s' );$( '#%2$s' ).val( '%3$s' );\">%3$s</a>"
                , UserEditDataModel.USER_PASSWORD_FORM_CONTROL, UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL, password);
    }

    public String getConfirmPasswordRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("UserRequirement: Confirm your password.", getLanguage()));

        return builder.toString();
    }

    public String getNameRequirement(final int minUserNameLength, final int maxUserNameLength) {
        final StringBuilder builder = new StringBuilder();

        final String minLen = String.valueOf(minUserNameLength);
        final String maxLen = String.valueOf(maxUserNameLength);

        builder.append(translatorService.translate("UserRequirement: Your public name in the club.", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        builder.append(translatorService.translate("UserRequirement: Your name should have length $1-$2, a-z, A-Z, 0-9 or _.", getLanguage(), minLen, maxLen));
        builder.append(DataRequirementService.HINT_LINE_BREAK);

        builder.append(translatorService.translate("UserRequirement: Will be visible for all users.", getLanguage()));

        return builder.toString();
    }

    public String getEmailRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("UserRequirement: Your email will never be shown to another users.", getLanguage()));

        return builder.toString();
    }

    public String getBirthdayRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("UserRequirement: Your birthday.", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: Will be visible for all users.", getLanguage()));

        return builder.toString();
    }

    public String getHomeSiteRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("UserRequirement: Your home site if exists.", getLanguage()));
        builder.append(DataRequirementService.HINT_LINE_BREAK);
        builder.append(translatorService.translate("UserRequirement: Will be visible for all users.", getLanguage()));

        return builder.toString();
    }
}
