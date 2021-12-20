package com.bordozer.jphoto.ui.controllers.users.edit;

import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import com.bordozer.jphoto.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserEditDataValidator implements Validator {

    public static final String USER_DATA_LOGIN = "User data: Login";
    public static final String USER_DATA_NAME = "User data: Name";
    public static final String USER_DATA_EMAIL = "User data: Email";
    public static final String USER_DATA_AVATAR = "User data: Avatar";
    public static final String USER_DATA_BIRTHDAY = "User data: Birthday";
    public static final String USER_DATA_MEMBERSHIP_TYPE = "User data: Membership type";
    public static final String USER_DATA_GENDER = "User data: Gender";
    public static final String USER_DATA_STATUS = "User data: Status";
    public static final String USER_DATA_REGISTRATION_TIME = "User data: Registered";
    public static final String USER_DATA_LAST_ACTIVITY_TIME = "User data: Last activity time";
    public static final String USER_DATA_LAST_PASSWORD = "User data: Password";
    public static final String USER_DATA_LAST_PASSWORD_REPEAT = "User data: Repeat password";
    public static final String USER_DATA_SITE = "User data: Site";
    public static final String USER_DATA_UI_LANGUAGE = "User data: UI Language";
    public static final String USER_DATA_PHOTOS_ON_PAGE = "User data: Photos on page";
    public static final String USER_DATA_SHOW_NUDE_CONTENT = "User data: Show nude content";
    public static final String USER_DATA_NOTIFICATION_EMAIL = "User data: Notification email";

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private TranslatorService translatorService;

    public boolean supports(Class<?> clazz) {
        return UserEditDataModel.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {

        UserEditDataModel model = (UserEditDataModel) target;

        validateLogin(errors, model.getLogin(), model.getUserId());

        if (model.isNew()) {
            usersSecurityService.validatePasswordCreation(model.getPassword(), model.getConfirmPassword(), errors);
        }

        validateName(errors, model.getName(), model.getUserId());

        validateEmail(errors, model.getEmail(), model.getUserId());

        validateBirthDate(errors, model.getDateOfBirth());

        validateMembershipType(errors, model.getMembershipTypeId());

        validateGender(errors, model.getUserGenderId());

        //		validateHomeSite( errors, user.getHomeSite() );
    }

    private void validateLogin(final Errors errors, final String userLogin, final int userId) {

        if (StringUtils.isEmpty(userLogin)) {
            errors.rejectValue(UserEditDataModel.USER_LOGIN_FORM_CONTROL, translatorService.translate("$1 should not be empty", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_LOGIN)));
            return;
        }

        final int loginLength = userLogin.length();
        final int minLoginLength = configurationService.getInt(ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH);
        final int maxLoginLength = configurationService.getInt(ConfigurationKey.SYSTEM_LOGIN_MAX_LENGTH);

        if (loginLength < minLoginLength) {
            final String translate = translatorService.translate("is too short. Min: $2, entered: $3", EnvironmentContext.getLanguage(), userLogin, String.valueOf(minLoginLength), String.valueOf(loginLength));
            final String errorCode = String.format("%s %s", FormatUtils.getFormattedFieldName(USER_DATA_LOGIN), translate);

            errors.rejectValue(UserEditDataModel.USER_LOGIN_FORM_CONTROL, errorCode);

            return;
        } else {
            if (loginLength > maxLoginLength) {
                String translate = translatorService.translate("is too long. Max: $2, entered: $3", EnvironmentContext.getLanguage(), userLogin, String.valueOf(maxLoginLength), String.valueOf(loginLength));
                final String errorCode = String.format("%s %s", FormatUtils.getFormattedFieldName(USER_DATA_LOGIN), translate);

                errors.rejectValue(UserEditDataModel.USER_LOGIN_FORM_CONTROL, errorCode);
                return;
            }
        }

        // User's login has to be unique
        final User checkUser = userService.loadByLogin(userLogin);
        if (checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId) {
            errors.rejectValue(UserEditDataModel.USER_LOGIN_FORM_CONTROL, translatorService.translate("$1 '$2' is busy", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_LOGIN), userLogin));
        }
    }

    private void validateName(final Errors errors, final String userName, final int userId) {

        if (StringUtils.isEmpty(userName)) {
            errors.rejectValue(UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate("$1 should not be empty", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_NAME)));
            return;
        }

        final int userNameLength = userName.length();
        final int minUserNameLength = configurationService.getInt(ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH);
        final int maxUserNameLength = configurationService.getInt(ConfigurationKey.SYSTEM_USER_NAME_MAX_LENGTH);

        if (userName.length() < minUserNameLength) {
            errors.rejectValue(UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate("$1 '$2' should be more then $3 symbols", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_NAME), userName, String.valueOf(minUserNameLength)));
            return;
        }

        if (userName.length() > maxUserNameLength) {
            errors.rejectValue(UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate("$1 '$2' should be less then $3 symbols", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_NAME), userName, String.valueOf(maxUserNameLength)));
            return;
        }

        // User's name has to be unique
        final User checkUser = userService.loadByName(userName);
        if (checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId) {
            errors.rejectValue(UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate("$1 '$2' is busy", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_NAME), userName));
        }
    }

    private void validateEmail(final Errors errors, final String userEmail, final int userId) {

        if (StringUtils.isEmpty(userEmail)) {
            errors.rejectValue(UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate("$1 should not be empty", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_EMAIL)));
            return;
        }

        if (!EmailValidator.getInstance().isValid(userEmail)) {
            errors.rejectValue(UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate("$1 '$2') has invalid format", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_EMAIL), userEmail));
            return;
        }

        // User's email has to be unique
        final User checkUser = userService.loadByEmail(userEmail);
        if (checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId) {
            errors.rejectValue(UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate("$1 '$2' is already presents in the system", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_EMAIL), userEmail));
        }
    }

    private void validateBirthDate(final Errors errors, final String dateOfBirth) {
        if (!dateUtilsService.validateDate(dateOfBirth)) {
            errors.rejectValue(UserEditDataModel.USER_DATE_OF_BIRTH_FORM_CONTROL, translatorService.translate("$1 is invalid", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_BIRTHDAY)));
        }
    }

    private void validateMembershipType(final Errors errors, final int membershipTypeId) {
        final UserMembershipType membershipType = UserMembershipType.getById(membershipTypeId);
        if (membershipType == null) {
            errors.rejectValue(UserEditDataModel.MEMBERSHIP_TYPE_FORM_CONTROL, translatorService.translate("Select your $1", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_MEMBERSHIP_TYPE)));
        }
    }

    private void validateGender(final Errors errors, final int userGenderId) {
        final UserGender userGender = UserGender.getById(userGenderId);
        if (userGender == null) {
            errors.rejectValue(UserEditDataModel.USER_GENDER_FORM_CONTROL, translatorService.translate("Please, select your $1", EnvironmentContext.getLanguage()
                    , FormatUtils.getFormattedFieldName(USER_DATA_GENDER)));
        }
    }
}
