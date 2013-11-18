package controllers.users.password.change;

import core.general.user.User;
import core.services.user.UsersSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

public class ChangeUserPasswordValidator implements Validator {

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return ChangeUserPasswordModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final ChangeUserPasswordModel model = ( ChangeUserPasswordModel ) target;

		validateOldPassword( model.getUser(), model.getOldPassword(), errors );

		if ( ! errors.hasErrors() ) {
			validateNewPassword( model, errors );
		}
	}

	private void validateOldPassword( final User user, final String password, final Errors errors ) {
		if ( ! usersSecurityService.isUserPasswordCorrect( user, password ) ) {
			errors.rejectValue( ChangeUserPasswordModel.FORM_CONTROL_OLD_PASSWORD, TranslatorUtils.translate( "Incorrect $1", FormatUtils.getFormattedFieldName( "Old password" ) ) );
		}
	}

	private void validateNewPassword( final ChangeUserPasswordModel model, final Errors errors ) {
		usersSecurityService.validatePasswordCreation( model.getPassword(), model.getConfirmPassword(), errors );
	}
}
