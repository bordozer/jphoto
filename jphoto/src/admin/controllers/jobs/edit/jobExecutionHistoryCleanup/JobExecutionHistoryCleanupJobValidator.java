package admin.controllers.jobs.edit.jobExecutionHistoryCleanup;

import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.FormatUtils;
import utils.NumberUtils;

import java.util.List;

public class JobExecutionHistoryCleanupJobValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;
	
	@Override
	public boolean supports( final Class<?> clazz ) {
		return JobExecutionHistoryCleanupJobModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final JobExecutionHistoryCleanupJobModel model = ( JobExecutionHistoryCleanupJobModel ) target;

		validateDays( model.getDeleteEntriesOlderThenDays(), errors );
		validateStatuses( model.getJobExecutionStatusIdsToDelete(), errors );
	}

	private void validateDays( final String leaveActivityForDays, final Errors errors ) {
		if ( ! NumberUtils.isNumeric( leaveActivityForDays ) ) {
			errors.rejectValue( JobExecutionHistoryCleanupJobModel.DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL,
								translatorService.translate( "$1 must be zero or a positive number", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "days" ) ) );
		}
	}

	private void validateStatuses( final List<String> jobExecutionStatusIdsToDelete, final Errors errors ) {
		if ( jobExecutionStatusIdsToDelete == null || jobExecutionStatusIdsToDelete.size() == 0 ) {
			errors.rejectValue( JobExecutionHistoryCleanupJobModel.DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL,
								translatorService.translate( "Select at least one $1", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "status" ) ) );
		}
	}
}
