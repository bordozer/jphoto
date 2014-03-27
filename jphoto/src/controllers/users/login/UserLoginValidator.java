package controllers.users.login;

import core.context.EnvironmentContext;
import core.general.user.User;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.user.UsersSecurityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

public class UserLoginValidator implements Validator {

	public static final String INCORRECT_LOGIN_PASSWORD_MESSAGE = String.format( "Incorrect %s or %s. Please, verify the data and try again."
	, FormatUtils.getFormattedFieldName( "Login" ), FormatUtils.getFormattedFieldName( "Password" ) ); // TODO

	@Autowired
	private UserService userService;

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( Class<?> clazz ) {
		return UserLoginModel.class.equals( clazz );
	}

	@Override
	public void validate( Object target, Errors errors ) {

		final UserLoginModel model = ( UserLoginModel ) target;

		if ( ! validateLoginAndPasswordEntered( errors, model ) ) {
			return;
		}

		validateUserExists( errors, model );

		if ( errors.getAllErrors().size() == 0 ) {
			validateAuthorization( model, errors );
		}

		if ( errors.getAllErrors().size() > 0 ) {
			model.getPageModel().setShowLoginForm( false );
		}
	}

	private boolean validateLoginAndPasswordEntered( final Errors errors, final UserLoginModel model ) {
		validateLoginEntered( model, errors );

		validatePasswordEntered( model, errors );

		return errors.getAllErrors().size() == 0;
	}

	private void validateLoginEntered( final UserLoginModel model, final Errors errors ) {
		final String login = model.getUserlogin();
		if ( StringUtils.isEmpty( login ) ) {
			final String errorCode = translatorService.translate( "Please, enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Login" ) );
			errors.rejectValue( UserLoginModel.LOGIN_FORM_LOGIN_CONTROL, errorCode );
			return;
		}
	}

	private void validatePasswordEntered( final UserLoginModel model, final Errors errors ) {
		final String password = model.getUserpassword();
		if ( StringUtils.isEmpty( password ) ) {
			final String errorCode = translatorService.translate( "Please, enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Password" ) );
			errors.rejectValue( UserLoginModel.LOGIN_FORM_PASSWORD_CONTROL, errorCode );
			return;
		}
	}

	private void validateUserExists( final Errors errors, final UserLoginModel model ) {
		final User user = userService.loadByLogin( model.getUserlogin() );
		if ( user == null ) {
			errors.rejectValue( UserLoginModel.LOGIN_FORM_LOGIN_CONTROL, INCORRECT_LOGIN_PASSWORD_MESSAGE );
		}
	}

	private void validateAuthorization( final UserLoginModel model, final Errors errors ) {
		final User user = userService.loadByLogin( model.getUserlogin() );
		final String password = model.getUserpassword();

		if ( ! usersSecurityService.isUserPasswordCorrect( user, password ) ) {
			errors.rejectValue( UserLoginModel.LOGIN_FORM_LOGIN_CONTROL, INCORRECT_LOGIN_PASSWORD_MESSAGE );
		}
	}
}
