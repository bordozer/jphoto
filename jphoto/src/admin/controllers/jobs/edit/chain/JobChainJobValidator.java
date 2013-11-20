package admin.controllers.jobs.edit.chain;

import admin.controllers.jobs.edit.SavedJobValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

import java.util.List;

public class JobChainJobValidator extends SavedJobValidator implements Validator {

	@Override
	public boolean supports( final Class<?> clazz ) {
		return JobChainJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final JobChainJobModel model = ( JobChainJobModel ) target;

		validateJobName( model, errors );

		validateJobSelected( model, errors );
	}

	private void validateJobSelected( final JobChainJobModel model, final Errors errors ) {
		final List<String> savedJobsIds = model.getSelectedSavedJobsIds();
		if ( savedJobsIds == null || savedJobsIds.size() == 0 ) {
			errors.rejectValue( JobChainJobModel.SELECTED_SAVED_JOBS_IDS_FORM_CONTROL, TranslatorUtils.translate( String.format( "Select at least one %s.", FormatUtils.getFormattedFieldName( "Job" ) ) ) );
		}
	}
}
