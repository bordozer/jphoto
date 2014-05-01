package json.photo.appraisal;

import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.UserUtils;

public class PhotoAppraisalFormValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.isAssignableFrom( PhotoAppraisalDTO.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoAppraisalDTO dto = ( PhotoAppraisalDTO ) target;

		validateUser( dto.getUserId(), errors );
	}

	private void validateUser( final int userId, final Errors errors ) {
		if ( ! UserUtils.isTheUserThatWhoIsCurrentUser( userId ) ) {
			final String errorMessage = translatorService.translate( "You are not the user who has loaded appraisal form. The user must have changed since then.", EnvironmentContext.getLanguage() );
			errors.rejectValue( "userId", "error code", errorMessage );
		}
	}
}
