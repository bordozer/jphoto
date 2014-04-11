package admin.controllers.jobs.edit.activityStream;

import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.FormatUtils;
import utils.NumberUtils;

public class ActivityStreamCleanupJobValidator implements Validator {
	
	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return ActivityStreamCleanupJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final ActivityStreamCleanupJobModel model = ( ActivityStreamCleanupJobModel ) target;

		validateLeaveActivityForDays( model.getLeaveActivityForDays(), errors );
	}

	private void validateLeaveActivityForDays( final String leaveActivityForDays, final Errors errors ) {
		if ( NumberUtils.convertToInt( leaveActivityForDays ) <= 0 ) {
			errors.rejectValue( ActivityStreamCleanupJobModel.LEAVE_ACTIVITY_FOR_DAYS_CONTROL,
								translatorService.translate( "Please, enter $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "days" ) ) );
		}
	}
}
