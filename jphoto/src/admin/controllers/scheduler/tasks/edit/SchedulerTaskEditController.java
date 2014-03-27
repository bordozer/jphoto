package admin.controllers.scheduler.tasks.edit;

import admin.jobs.general.SavedJob;
import admin.services.jobs.SavedJobService;
import admin.services.scheduler.SchedulerService;
import core.context.EnvironmentContext;
import core.enums.SchedulerTaskProperty;
import core.general.base.CommonProperty;
import core.general.executiontasks.AbstractExecutionTask;
import core.general.executiontasks.ExecutionTaskFactory;
import core.general.executiontasks.ExecutionTaskType;
import core.general.scheduler.SchedulerTask;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.google.common.collect.Maps.newLinkedHashMap;

@SessionAttributes( SchedulerTaskEditController.MODEL_NAME )
@Controller
@RequestMapping( "scheduler/tasks" )
public class SchedulerTaskEditController {

	private final static String VIEW = "admin/scheduler/tasks/edit/SchedulerTaskEdit";
	public static final String MODEL_NAME = "schedulerTaskEditModel";

	@Autowired
	private SchedulerTaskEditValidator schedulerTaskEditValidator;

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private SavedJobService savedJobService;
	
	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( schedulerTaskEditValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public SchedulerTaskEditModel prepareModel() {
		return new SchedulerTaskEditModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "{scheduledTaskId}/info/" )
	public String schedulerTaskInfo( final @PathVariable( "scheduledTaskId" ) int scheduledTaskId, final @ModelAttribute( MODEL_NAME ) SchedulerTaskEditModel model ) {
		return UrlUtilsServiceImpl.UNDER_CONSTRUCTION_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String schedulerTaskNew( final @ModelAttribute( MODEL_NAME ) SchedulerTaskEditModel model ) {

		model.clear();

		final ExecutionTaskType initTaskType = ExecutionTaskType.ONCE;

		model.setSchedulerTaskName( translatorService.translate( "New scheduler task", EnvironmentContext.getLanguage() ) );
		model.setExecutionTaskTypeId( initTaskType.getId() );
		model.setStartTaskDate( dateUtilsService.formatDate( dateUtilsService.getCurrentDate() ) );
		model.setSchedulerTaskTime( dateUtilsService.formatTimeShort( dateUtilsService.getFirstSecondOfToday() ) );
		model.setSkipMissedExecutions( true );
		model.setSavedJobs( getSavedJobs() );
		model.setSelectedTaskType( initTaskType );
		model.setPeriodicalTaskHours( SchedulerTaskEditModel.HOURS );

		model.setPageTitleData( pageTitleAdminUtilsService.getAdminSchedulerNewData() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{scheduledTaskId}/edit/" )
	public String schedulerTaskEdit( final @PathVariable( "scheduledTaskId" ) int scheduledTaskId, final @ModelAttribute( MODEL_NAME ) SchedulerTaskEditModel model ) {

		model.clear();

		final SchedulerTask schedulerTask = schedulerService.load( scheduledTaskId );
		final ExecutionTaskType taskType = schedulerTask.getTaskType();

		model.setSchedulerTaskId( scheduledTaskId );
		model.setSchedulerTaskName( schedulerTask.getName() );
		model.setExecutionTaskTypeId( taskType.getId() );
		model.setSavedJobId( schedulerTask.getSavedJobId() );
		model.setSavedJobs( getSavedJobs() );
		model.setSelectedTaskType( taskType );

		model.setSchedulerTaskSavedParameters( schedulerTask.getDescription() );

		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = schedulerTask.getExecutionTask().getParametersMap();
		initModelFromExecutionTaskParameterMap( model, parametersMap );

		model.setPageTitleData( pageTitleAdminUtilsService.getAdminSchedulerEditData( model.getSchedulerTaskName() ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "{scheduledTaskId}/delete/" )
	public String schedulerTaskDelete( final @PathVariable( "scheduledTaskId" ) int scheduledTaskId, final @ModelAttribute( MODEL_NAME ) SchedulerTaskEditModel model ) throws SchedulerException {

		schedulerService.delete( scheduledTaskId );

		return String.format( "redirect:%s", urlUtilsService.getAdminSchedulerTaskListLink() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/submit/" )
	public String schedulerTaskSubmit( @Valid final @ModelAttribute( MODEL_NAME ) SchedulerTaskEditModel model, final BindingResult result ) throws SchedulerException {

		if ( model.getSchedulerTaskId() == 0 ) {
			model.setPageTitleData( pageTitleAdminUtilsService.getAdminSchedulerNewData() );
		} else {
			model.setPageTitleData( pageTitleAdminUtilsService.getAdminSchedulerEditData( model.getSchedulerTaskName() ) );
		}

		model.setSelectedTaskType( ExecutionTaskType.getById( model.getExecutionTaskTypeId() ) );

		if ( model.getFormAction().equals( "reload" ) ) {
			return VIEW;
		}

		model.setBindingResult( result );
		if ( result.hasErrors() ) {
			return VIEW;
		}

		final SchedulerTask schedulerTask = createSchedulerTaskFromModel( model );

		if ( ! schedulerService.save( schedulerTask ) ) {
			final Language language = EnvironmentContext.getLanguage();
			result.reject( translatorService.translate( "Registration error", language ), translatorService.translate( "Error saving data to DB", language ) );
			return VIEW;
		}

		return String.format( "redirect:%s", urlUtilsService.getAdminSchedulerTaskListLink() );
	}

	private List<SavedJob> getSavedJobs() {
		final List<SavedJob> savedJobs = savedJobService.loadAllActive();

		Collections.sort( savedJobs, new Comparator<SavedJob>() {
			@Override
			public int compare( final SavedJob o1, final SavedJob o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );

		return savedJobs;
	}

	private void initModelFromExecutionTaskParameterMap( final SchedulerTaskEditModel model, final Map<SchedulerTaskProperty, CommonProperty> parametersMap ) {

		final CommonProperty startTaskDate = parametersMap.get( SchedulerTaskProperty.PROPERTY_START_TASK_DATE );
		model.setSchedulerTaskTime( dateUtilsService.formatTime( startTaskDate.getValueTime( dateUtilsService ) ) );
		model.setStartTaskDate( dateUtilsService.formatDate( startTaskDate.getValueTime( dateUtilsService ) ) );

		final CommonProperty endTaskDate = parametersMap.get( SchedulerTaskProperty.PROPERTY_END_TASK_DATE );
		if ( endTaskDate != null && StringUtils.isNotEmpty( endTaskDate.getValue() ) ) {
			model.setEndTaskTime( dateUtilsService.formatTime( endTaskDate.getValueTime( dateUtilsService ) ) );
			model.setEndTaskDate( dateUtilsService.formatDate( endTaskDate.getValueTime( dateUtilsService ) ) );
		}

		final CommonProperty skipMissedExecutionProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS );
		if ( skipMissedExecutionProperty != null ) {
			model.setSkipMissedExecutions( skipMissedExecutionProperty.getValueBoolean() );
		}

		final CommonProperty periodProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_PERIOD );
		if ( periodProperty != null ) {
			model.setPeriodicalTaskPeriod( periodProperty.getValue() );
			model.setPeriodicalTaskPeriodUnitId( parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT ).getValueInt() );
		}

		final CommonProperty hoursProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_TASK_HOURS );
		if ( hoursProperty != null ) {
			model.setPeriodicalTaskHours( hoursProperty.getValueListString() );
		}

		final CommonProperty weekdaysIdsProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS );
		if ( weekdaysIdsProperty != null ) {
			model.setDailyTaskWeekdayIds( weekdaysIdsProperty.getValueListString() );
		}

		final CommonProperty monthsIdsProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS );
		if ( monthsIdsProperty != null ) {
			model.setMonthlyTaskMonthIds( monthsIdsProperty.getValueListString() );
		}

		final CommonProperty dayOfMonthProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH );
		if ( dayOfMonthProperty != null ) {
			model.setMonthlyDayOfMonth( dayOfMonthProperty.getValue() );
		}

		final CommonProperty taskActiveProperty = parametersMap.get( SchedulerTaskProperty.PROPERTY_IS_ACTIVE );
		model.setSchedulerTaskActive( taskActiveProperty.getValueBoolean() );
	}

	private SchedulerTask createSchedulerTaskFromModel( final SchedulerTaskEditModel model ) {
		final ExecutionTaskType executionTaskType = ExecutionTaskType.getById( model.getExecutionTaskTypeId() );

		final SchedulerTask schedulerTask = new SchedulerTask();
		schedulerTask.setSavedJobId( model.getSavedJobId() );
		schedulerTask.setId( model.getSchedulerTaskId() );
		schedulerTask.setName( model.getSchedulerTaskName() );
		schedulerTask.setTaskType( executionTaskType );

		final Map<SchedulerTaskProperty, CommonProperty> parametersMap = newLinkedHashMap();

		parametersMap.put( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS, new CommonProperty( SchedulerTaskProperty.PROPERTY_SKIP_MISSED_EXECUTIONS.getId(), model.isSkipMissedExecutions() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_TASK_PERIOD, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_PERIOD.getId(), model.getPeriodicalTaskPeriod() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_PERIOD_UNIT.getId(), model.getPeriodicalTaskPeriodUnitId() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_TASK_HOURS, new CommonProperty( SchedulerTaskProperty.PROPERTY_TASK_HOURS.getId(), model.getPeriodicalTaskHours() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS, new CommonProperty( SchedulerTaskProperty.PROPERTY_DAILY_TASK_WEEKDAY_IDS.getId(), model.getDailyTaskWeekdayIds() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS, new CommonProperty( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_MONTH_IDS.getId(), model.getMonthlyTaskMonthIds() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH, new CommonProperty( SchedulerTaskProperty.PROPERTY_MONTHLY_TASK_DAY_OF_MONTH.getId(), model.getMonthlyDayOfMonth() ) );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_IS_ACTIVE, new CommonProperty( SchedulerTaskProperty.PROPERTY_IS_ACTIVE.getId(), model.isSchedulerTaskActive() ) );


		final Date executionTime = dateUtilsService.parseDateTime( model.getStartTaskDate(), model.getSchedulerTaskTime() );
		parametersMap.put( SchedulerTaskProperty.PROPERTY_START_TASK_DATE
			, new CommonProperty( SchedulerTaskProperty.PROPERTY_START_TASK_DATE.getId(), executionTime, dateUtilsService ) );

		if ( StringUtils.isNotEmpty( model.getEndTaskDate() ) ) {
			final Date endTaskDate;
			if ( StringUtils.isNotEmpty( model.getEndTaskTime() ) ) {
				endTaskDate = dateUtilsService.parseDateTime( model.getEndTaskDate(), model.getEndTaskTime() );
			} else {
				endTaskDate = dateUtilsService.parseDate( model.getEndTaskDate() );
			}
			parametersMap.put( SchedulerTaskProperty.PROPERTY_END_TASK_DATE, new CommonProperty( SchedulerTaskProperty.PROPERTY_END_TASK_DATE.getId(), endTaskDate, dateUtilsService ) );
		}

		final AbstractExecutionTask executionTask = ExecutionTaskFactory.createInstance( schedulerTask.getTaskType(), dateUtilsService );
		executionTask.initTaskParameters( parametersMap );
		schedulerTask.setExecutionTask( executionTask );

		return schedulerTask;
	}
}
