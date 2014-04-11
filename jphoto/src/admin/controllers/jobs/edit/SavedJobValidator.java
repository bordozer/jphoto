package admin.controllers.jobs.edit;

import admin.jobs.general.SavedJob;
import admin.services.jobs.SavedJobService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import ui.context.EnvironmentContext;
import utils.FormatUtils;
import utils.NumberUtils;

public class SavedJobValidator {

	@Autowired
	private SavedJobService savedJobService;

	@Autowired
	protected TranslatorService translatorService;

	public void validateJobName( final AbstractAdminJobModel model, final Errors errors ) {

		if( ! model.isSaveJobMode() ) {
			return;
		}

		final String jobName = model.getJobName();

		if ( StringUtils.isEmpty( jobName ) ) {
			errors.rejectValue( AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL, translatorService.translate( "Enter $1.", getLanguage(), FormatUtils.getFormattedFieldName( "Job name" ) ) );
			return;
		}

		final SavedJob savedJob = savedJobService.loadByName( jobName );
		if ( savedJob != null && ( savedJob.getId() != model.getSavedJobId() || model.isSaveAsCopy() ) ) {
			errors.rejectValue( AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL,
								translatorService.translate( "Job name '$1' already exists.", getLanguage(), jobName ) );
			return;
		}
	}

	protected void validateNonZeroPositiveNumber( final String number, final Errors errors, final String fieldName, final String formControl ) {
		if ( StringUtils.isEmpty( number ) ) {
			errors.rejectValue( formControl, translatorService.translate( "Enter $1.", getLanguage(), FormatUtils.getFormattedFieldName( fieldName ) ) );
			return;
		}

		if ( ! NumberUtils.isNumeric( number ) ) {
			errors.rejectValue( formControl, translatorService.translate( "$1 must be a positive number.", getLanguage(), FormatUtils.getFormattedFieldName( fieldName ) ) );
			return;
		}

		final int userQty = Integer.parseInt( number );
		if ( userQty <= 0 ) {
			errors.rejectValue( formControl, translatorService.translate( "$1 must be a positive number.", getLanguage(), FormatUtils.getFormattedFieldName( fieldName ) ) );
			return;
		}
	}

	protected boolean validatePositiveNumber( final String number, final Errors errors, final String formControl, final String fieldName ) {
		if ( !NumberUtils.isNumeric( number ) || Integer.parseInt( number ) <= 0 ) {
			errors.rejectValue( formControl, translatorService.translate( "$1 must be a positive number", getLanguage(), FormatUtils.getFormattedFieldName( fieldName ) ) );
			return true;
		}
		return false;
	}

	protected boolean validateZeroOrPositiveNumber( final String number, final Errors errors, final String formControl, final String fieldName ) {
		if ( NumberUtils.convertToInt( number ) < 0 ) {
			errors.rejectValue( formControl, translatorService.translate( "$1 must be zero or positive", getLanguage(), FormatUtils.getFormattedFieldName( fieldName ) ) );
			return true;
		}
		return false;
	}

	private Language getLanguage() {
		return EnvironmentContext.getCurrentUser().getLanguage();
	}
}
