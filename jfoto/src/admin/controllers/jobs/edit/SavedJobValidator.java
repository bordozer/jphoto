package admin.controllers.jobs.edit;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.jobs.general.SavedJob;
import core.services.job.SavedJobService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import utils.FormatUtils;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class SavedJobValidator {

	@Autowired
	private SavedJobService savedJobService;

	public void validateJobName( final AbstractAdminJobModel model, final Errors errors ) {

		if( ! model.isSaveJobMode() ) {
			return;
		}

		final String jobName = model.getJobName();

		if ( StringUtils.isEmpty( jobName ) ) {
			errors.rejectValue( AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL, TranslatorUtils.translate( String.format( "Enter %s.", FormatUtils.getFormattedFieldName( "Job name" ) ) ) );
			return;
		}

		final SavedJob savedJob = savedJobService.loadByName( jobName );
		if ( savedJob != null && ( savedJob.getId() != model.getSavedJobId() || model.isSaveAsCopy() ) ) {
			errors.rejectValue( AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL,
								TranslatorUtils.translate( "Job name '$1' already exists.", jobName ) );
			return;
		}
	}

	protected void validateNonZeroPositiveNumber( final String number, final Errors errors, final String fieldName, final String formControl ) {
		if ( StringUtils.isEmpty( number ) ) {
			errors.rejectValue( formControl, TranslatorUtils.translate( String.format( "Enter %s.", FormatUtils.getFormattedFieldName( fieldName ) ) ) );
			return;
		}

		if ( ! NumberUtils.isNumeric( number ) ) {
			errors.rejectValue( formControl, TranslatorUtils.translate( String.format( "%s must be a positive number.", FormatUtils.getFormattedFieldName( fieldName ) ) ) );
			return;
		}

		final int userQty = Integer.parseInt( number );
		if ( userQty <= 0 ) {
			errors.rejectValue( formControl, TranslatorUtils.translate( String.format( "%s must be a positive number.", FormatUtils.getFormattedFieldName( fieldName ) ) ) );
			return;
		}
	}

	protected boolean validatePositiveNumber( final String number, final Errors errors, final String formControl, final String fieldName ) {
		if ( !NumberUtils.isNumeric( number ) || Integer.parseInt( number ) <= 0 ) {
			errors.rejectValue( formControl, TranslatorUtils.translate( String.format( "%s must be a positive number", FormatUtils.getFormattedFieldName( fieldName ) ) ) );
			return true;
		}
		return false;
	}

	protected boolean validateZeroOrPositiveNumber( final String number, final Errors errors, final String formControl, final String fieldName ) {
		if ( NumberUtils.convertToInt( number ) < 0 ) {
			errors.rejectValue( formControl, TranslatorUtils.translate( String.format( "%s must be zero or positive", FormatUtils.getFormattedFieldName( fieldName ) ) ) );
			return true;
		}
		return false;
	}
}
