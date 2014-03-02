package admin.controllers.scheduler.tasks.edit;

import admin.services.scheduler.SchedulerService;
import core.general.executiontasks.ExecutionTaskType;
import core.general.executiontasks.PeriodUnit;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

import java.util.List;

public class SchedulerTaskEditValidator implements Validator {

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return SchedulerTaskEditModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final SchedulerTaskEditModel model = ( SchedulerTaskEditModel ) target;

		final ExecutionTaskType executionTaskType = ExecutionTaskType.getById( model.getExecutionTaskTypeId() );

		validateName( model, errors );

		validateTime( model, errors );

		validateDate( model, errors );

		validateJob( model, errors );

		switch ( executionTaskType ) {
			case ONCE:
				validateOnceExecutionTask( model, errors );
				break;
			case PERIODICAL:
				validatePeriodicalExecutionTask( model, errors );
				break;
			case DAILY:
				validateDailyExecutionTask( model, errors );
				break;
			case MONTHLY:
				validateMonthlyExecutionTask( model, errors );
				break;
		}
	}

	private void validateName( final SchedulerTaskEditModel model, final Errors errors ) {
		final String schedulerTaskName = model.getSchedulerTaskName();
		if ( StringUtils.isEmpty( schedulerTaskName ) ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_NAME_CONTROL
				, TranslatorUtils.translate( String.format( "Enter %s", FormatUtils.getFormattedFieldName( "Name" ) ) ) );
			return;
		}

		final int schedulerTaskId = schedulerService.loadIdByName( schedulerTaskName );
		if ( schedulerTaskId > 0 && schedulerTaskId != model.getSchedulerTaskId() ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_NAME_CONTROL
				, TranslatorUtils.translate( String.format( "%s already exists", FormatUtils.getFormattedFieldName( "Name" ) ) ) );
		}
	}

	private void validateTime( final SchedulerTaskEditModel model, final Errors errors ) {
		final String taskTime = model.getSchedulerTaskTime();
		if ( ! dateUtilsService.validateTime( taskTime ) ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_TIME_CONTROL
				, TranslatorUtils.translate( String.format( "%s has invalid data", FormatUtils.getFormattedFieldName( "Execution time" ) ) ) );
		}
	}

	private void validateJob( final SchedulerTaskEditModel model, final Errors errors ) {
		if ( model.getSavedJobId() == 0 ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_JOB_ID_CONTROL
				, TranslatorUtils.translate( String.format( "Select %s", FormatUtils.getFormattedFieldName( "Job" ) ) ) );
		}
	}

	private void validateOnceExecutionTask( final SchedulerTaskEditModel model, final Errors errors ) {
//		validateDate( model, errors );
	}

	private void validatePeriodicalExecutionTask( final SchedulerTaskEditModel model, final Errors errors ) {
//		validateDate( model, errors );

		if ( StringUtils.isEmpty( model.getPeriodicalTaskPeriod() ) ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_PERIODICAL_TASK_PERIOD_CONTROL
				, TranslatorUtils.translate( String.format( "Enter %s", FormatUtils.getFormattedFieldName( "Interval" ) ) ) );
		}

		if ( model.getPeriodicalTaskPeriodUnitId() != PeriodUnit.HOUR.getId() ) {
			final List<String> periodicalTaskHours = model.getPeriodicalTaskHours();
			if ( periodicalTaskHours == null || periodicalTaskHours.size() == 0 ) {
				errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_PERIODICAL_TASK_HOURS_CONTROL
					, TranslatorUtils.translate( String.format( "Select at least one %s", FormatUtils.getFormattedFieldName( "Hour" ) ) ) );
			}
		}
	}

	private void validateDailyExecutionTask( final SchedulerTaskEditModel model, final Errors errors ) {
		final List<String> dailyTaskWeekdayIds = model.getDailyTaskWeekdayIds();
		if ( dailyTaskWeekdayIds == null || dailyTaskWeekdayIds.isEmpty() ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_DAILY_TASK_WEEKDAY_IDS_CONTROL
				, TranslatorUtils.translate( String.format( "Select at least one %s", FormatUtils.getFormattedFieldName( "Day of week" ) ) ) );
		}
	}

	private void validateMonthlyExecutionTask( final SchedulerTaskEditModel model, final Errors errors ) {
		final List<String> monthlyTaskMonthIds = model.getMonthlyTaskMonthIds();

		final String month = model.getMonthlyDayOfMonth();
		if ( month != null && month.equals( "0" ) ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_MONTHLY_TASK_DAY_OF_MONTH_CONTROL
				, TranslatorUtils.translate( String.format( "Enter %s", FormatUtils.getFormattedFieldName( "Day of month" ) ) ) );
		}

		if ( monthlyTaskMonthIds == null || monthlyTaskMonthIds.isEmpty() ) {
			errors.rejectValue( SchedulerTaskEditModel.SCHEDULER_TASK_MONTHLY_TASK_WEEKDAY_IDS_CONTROL
				, TranslatorUtils.translate( String.format( "Select at least one %s", FormatUtils.getFormattedFieldName( "Month" ) ) ) );
		}
	}

	private void validateDate( final SchedulerTaskEditModel model, final Errors errors ) {
		final String taskDate = model.getStartTaskDate();
		if ( StringUtils.isEmpty( taskDate ) ) {
			errors.rejectValue( SchedulerTaskEditModel.START_TASK_DATE_CONTROL
				, TranslatorUtils.translate( String.format( "Enter %s", FormatUtils.getFormattedFieldName( "Execution date" ) ) ) );
			return;
		}

		if ( ! dateUtilsService.validateDate( model.getStartTaskDate() ) ) {
			errors.rejectValue( SchedulerTaskEditModel.START_TASK_DATE_CONTROL
				, TranslatorUtils.translate( String.format( "%s has invalid data", FormatUtils.getFormattedFieldName( "Execution date" ) ) ) );
		}
	}
}
