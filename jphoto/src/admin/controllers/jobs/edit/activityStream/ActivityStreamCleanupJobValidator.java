package admin.controllers.jobs.edit.activityStream;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class ActivityStreamCleanupJobValidator implements Validator {

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
								TranslatorUtils.translate( String.format( "Please, enter %s", FormatUtils.getFormattedFieldName( "days" ) ) ) );
		}
	}
}
