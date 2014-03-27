package controllers.users.list;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

import java.util.List;

public class FilterValidator implements Validator {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return UserFilterModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UserFilterModel model = ( UserFilterModel ) target;

		validateName( model, errors );

		validateMembership( model, errors );
	}

	private void validateName( final UserFilterModel model, final Errors errors ) {
		final int minUserNameLength = configurationService.getInt( ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH );
		final String filterUserName = model.getFilterUserName();
		if ( StringUtils.isNotEmpty( filterUserName ) && filterUserName.length() < minUserNameLength ) {
			errors.rejectValue( UserFilterModel.USER_NAME_FORM_CONTROL, translatorService.translate( "$1 should be at least $2 symbols", EnvironmentContext.getLanguage()
				, FormatUtils.getFormattedFieldName( "Name" ), String.valueOf( minUserNameLength ) ) );
		}
	}

	private void validateMembership( final UserFilterModel model, final Errors errors ) {
		final List<Integer> membershipTypeIds = model.getMembershipTypeList();
		if ( membershipTypeIds == null ) {
			errors.rejectValue( "membershipTypeList", translatorService.translate( "Select at least one $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Membership type" ) ) );
		}
	}
}
