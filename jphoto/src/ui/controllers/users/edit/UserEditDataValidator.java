package ui.controllers.users.edit;

import core.enums.UserGender;
import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.user.UsersSecurityService;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.FormatUtils;

public class UserEditDataValidator implements Validator {

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

	public boolean supports( Class<?> clazz ) {
		return UserEditDataModel.class.equals( clazz );
	}

	public void validate( Object target, Errors errors ) {

		UserEditDataModel model = ( UserEditDataModel ) target;

		validateLogin( errors, model.getLogin(), model.getUserId() );

		if ( model.isNew() ) {
			usersSecurityService.validatePasswordCreation( model.getPassword(), model.getConfirmPassword(), errors );
		}

		validateName( errors, model.getName(), model.getUserId() );

		validateEmail( errors, model.getEmail(), model.getUserId() );

		validateBirthDate( errors, model.getDateOfBirth() );

		validateMembershipType( errors, model.getMembershipTypeId() );

		validateGender( errors, model.getUserGenderId() );

//		validateHomeSite( errors, user.getHomeSite() );
	}

	private void validateLogin( final Errors errors, final String userLogin, final int userId ) {

		if ( StringUtils.isEmpty( userLogin ) ) {
			errors.rejectValue( UserEditDataModel.USER_LOGIN_FORM_CONTROL, translatorService.translate( "$1 should not be empty", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Login" ) ) );
			return;
		}

		final int loginLength = userLogin.length();
		final int minLoginLength = configurationService.getInt( ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH );
		final int maxLoginLength = configurationService.getInt( ConfigurationKey.SYSTEM_LOGIN_MAX_LENGTH );

		if ( loginLength < minLoginLength ) {
			final String translate = translatorService.translate( "is too short. Min: $2, entered: $3", EnvironmentContext.getLanguage(), userLogin, String.valueOf( minLoginLength ), String.valueOf( loginLength ) );
			final String errorCode = String.format( "%s %s", FormatUtils.getFormattedFieldName( "Login" ), translate );

			errors.rejectValue( UserEditDataModel.USER_LOGIN_FORM_CONTROL, errorCode );

			return;
		} else {
			if ( loginLength > maxLoginLength ) {
				String translate = translatorService.translate( "is too long. Max: $2, entered: $3", EnvironmentContext.getLanguage(), userLogin, String.valueOf( maxLoginLength ), String.valueOf( loginLength ) );
				final String errorCode = String.format( "%s %s", FormatUtils.getFormattedFieldName( "Login" ), translate );

				errors.rejectValue( UserEditDataModel.USER_LOGIN_FORM_CONTROL, errorCode );
				return;
			}
		}

		// User's login has to be unique
		final User checkUser = userService.loadByLogin( userLogin );
		if ( checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId ) {
			errors.rejectValue( UserEditDataModel.USER_LOGIN_FORM_CONTROL, translatorService.translate( "$1 '$2' is busy", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "login" ), userLogin ) );
		}
	}

	private void validateName( final Errors errors, final String userName, final int userId ) {

		if ( StringUtils.isEmpty( userName ) ) {
			errors.rejectValue( UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate( "$1 should not be empty", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Name" ) ) );
			return;
		}

		final int userNameLength = userName.length();
		final int minUserNameLength = configurationService.getInt( ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH );
		final int maxUserNameLength = configurationService.getInt( ConfigurationKey.SYSTEM_USER_NAME_MAX_LENGTH );

		if ( userName.length() < minUserNameLength ) {
			errors.rejectValue( UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate( "$1 '$2' should be more then $3 symbols", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Name" ), userName, String.valueOf( minUserNameLength ) ) );
			return;
		}

		if ( userName.length() > maxUserNameLength ) {
			errors.rejectValue( UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate( "$1 '$2' should be less then $3 symbols", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Name" ), userName, String.valueOf( maxUserNameLength ) ) );
			return;
		}

		// User's name has to be unique
		final User checkUser = userService.loadByName( userName );
		if ( checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId ) {
			errors.rejectValue( UserEditDataModel.USER_NAME_FORM_CONTROL, translatorService.translate( "$1 '$2' is busy", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "name" ), userName ) );
		}
	}

	private void validateEmail( final Errors errors, final String userEmail, final int userId ) {

		if ( StringUtils.isEmpty( userEmail ) ) {
			errors.rejectValue( UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate( "$1 should not be empty", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Email" ) ) );
			return;
		}

		if ( !EmailValidator.getInstance().isValid( userEmail ) ) {
			errors.rejectValue( UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate( "$1 '$2') has invalid format", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Email" ), userEmail ) );
			return;
		}

		// User's email has to be unique
		final User checkUser = userService.loadByEmail( userEmail );
		if ( checkUser != null && checkUser.getId() > 0 && checkUser.getId() != userId ) {
			errors.rejectValue( UserEditDataModel.USER_EMAIL_FORM_CONTROL, translatorService.translate( "$1 '$2' is already presents in the system", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "email" ), userEmail ) );
		}
	}

	private void validateBirthDate( final Errors errors, final String dateOfBirth ) {
		if ( ! dateUtilsService.validateDate( dateOfBirth ) ) {
			errors.rejectValue( UserEditDataModel.USER_DATE_OF_BIRTH_FORM_CONTROL, translatorService.translate( "$1 is invalid", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Birthday" ) ) );
		}
	}

	private void validateHomeSite( final Errors errors, final String homeSite ) {
		/*
		String[] schemes = { "http" };
 		UrlValidator urlValidator = new UrlValidator( schemes );
		if ( StringUtils.isNotEmpty( homeSite ) && ! urlValidator.isValid( homeSite ) ) {
			errors.rejectValue( UserEditDataModel.USER_HOME_SITE_FORM_CONTROL, translatorService.translate( "Entered site's URL has invalid format." ) );
		}*/
	}

	private void validateMembershipType( final Errors errors, final int membershipTypeId ) {
		final UserMembershipType membershipType = UserMembershipType.getById( membershipTypeId );
		if ( membershipType == null ) {
			errors.rejectValue( UserEditDataModel.MEMBERSHIP_TYPE_FORM_CONTROL, translatorService.translate( "Select your $1", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Membership type" ) ) );
		}
	}

	private void validateGender( final Errors errors, final int userGenderId ) {
		final UserGender userGender = UserGender.getById( userGenderId );
		if ( userGender == null ) {
			errors.rejectValue( UserEditDataModel.USER_GENDER_FORM_CONTROL, translatorService.translate( "Please, select your $1", EnvironmentContext.getLanguage()
					, FormatUtils.getFormattedFieldName( "Gender" ) ) );
		}
	}
}
