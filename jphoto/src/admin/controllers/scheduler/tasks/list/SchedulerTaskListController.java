package admin.controllers.scheduler.tasks.list;

import admin.jobs.general.SavedJob;
import admin.services.jobs.SavedJobService;
import admin.services.scheduler.ScheduledTasksExecutionService;
import admin.services.scheduler.SchedulerService;
import core.general.scheduler.SchedulerTask;
import core.log.LogHelper;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import utils.ListUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping( "scheduler" )
public class SchedulerTaskListController {

	private final static String VIEW = "admin/scheduler/tasks/list/SchedulerTaskList";
	private static final String MODEL_NAME = "schedulerTaskListModel";

	public static final String SCHEDULER_TASK_LIST_GROUP_ACTION_ACTIVATE = "activate";
	public static final String SCHEDULER_TASK_LIST_GROUP_ACTION_DEACTIVATE = "deactivate";
	public static final String SCHEDULER_TASK_LIST_GROUP_ACTION_DELETE = "delete";

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private SavedJobService savedJobService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private ScheduledTasksExecutionService scheduledTasksExecutionService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private SystemVarsService systemVarsService;

	private final LogHelper log = new LogHelper( SchedulerTaskListController.class );

	@ModelAttribute( MODEL_NAME )
	public SchedulerTaskListModel prepareModel() throws SchedulerException {
		final SchedulerTaskListModel model = new SchedulerTaskListModel();

		model.setPageTitleData( breadcrumbsAdminService.getAdminSchedulerTaskListBreadcrumbs() );

		setScheduledTaskData( model );

		final Map<Integer, SavedJob> map = newLinkedHashMap();
		final List<SavedJob> savedJobs = savedJobService.loadAll();
		for ( final SavedJob savedJob : savedJobs ) {
			map.put( savedJob.getId(), savedJob );
		}
		model.setSavedJobMap( map );

		model.setSchedulerRunning( scheduledTasksExecutionService.isRunning() );
		model.setSchedulerEnabled( systemVarsService.isSchedulerEnabled() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/tasks/" )
	public String schedulerTaskList( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {
		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/run/" )
	public String schedulerStart( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {

		if ( ! systemVarsService.isSchedulerEnabled() ) {
			return getForwardToListView();
		}

		final Language language = EnvironmentContext.getLanguage();
		try {
			log.info( "Starting scheduler" );
			scheduledTasksExecutionService.start();
			EnvironmentContext.getEnvironment().setHiMessage( translatorService.translate( "The scheduler has been started", language ) );
		} catch ( SchedulerException e ) {
			model.getBindingResult().reject( translatorService.translate( "Scheduler error", language ), translatorService.translate( "Can not start Scheduler", language ) );
			log.error( "Can not start Scheduler" );
		}

		return getForwardToListView();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/" )
	public String schedulerStop( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {
		final Language language = EnvironmentContext.getLanguage();
		try {
			log.info( "Stopping scheduler" );
			scheduledTasksExecutionService.standby();

			EnvironmentContext.getEnvironment().setHiMessage( translatorService.translate( "The scheduler has been stopped", language ) );
		} catch ( SchedulerException e ) {
			model.getBindingResult().reject( translatorService.translate( "Scheduler error", language ), translatorService.translate( "Can not stop Scheduler", language ) );
			log.error( "Can not stop Scheduler" );
		}

		return getForwardToListView();
	}

	@RequestMapping( method = RequestMethod.POST, value = "/tasks/" )
	public String schedulerTaskActivate( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) throws SchedulerException {

		final String formAction = model.getSchedulerTasksFormAction();
		final List<Integer> ids = ListUtils.convertStringListToInteger( Arrays.asList( model.getSchedulerTaskCheckbox() ) );

		final Language language = EnvironmentContext.getLanguage();
		switch ( formAction ) {
			case SCHEDULER_TASK_LIST_GROUP_ACTION_ACTIVATE:
				schedulerService.activateSchedulerTasks( ids );
				EnvironmentContext.getEnvironment().setHiMessage( translatorService.translate( "$1 task(s) have been activated", language, String.valueOf( ids.size() ) ) );
				break;
			case SCHEDULER_TASK_LIST_GROUP_ACTION_DEACTIVATE:
				schedulerService.deactivateSchedulerTasks( ids );
				EnvironmentContext.getEnvironment().setHiMessage( translatorService.translate( "$1 task(s) have been deactivated", language, String.valueOf( ids.size() ) ) );
				break;
			case SCHEDULER_TASK_LIST_GROUP_ACTION_DELETE:
				for ( final int id : ids ) {
					schedulerService.delete( id );
				}
				EnvironmentContext.getEnvironment().setHiMessage( translatorService.translate( "$1 task(s) have been deleted", language, String.valueOf( ids.size() ) ) );
				break;
		}

		return getForwardToListView();
	}

	private void setScheduledTaskData( final SchedulerTaskListModel model ) throws SchedulerException {
		final List<SchedulerTask> schedulerTasks = schedulerService.loadAll();

		final List<ScheduledTaskData> scheduledTaskDataMap = newArrayList();
		for ( final SchedulerTask schedulerTask : schedulerTasks ) {
			final int schedulerTaskId = schedulerTask.getId();

			final ScheduledTaskData scheduledTaskData = new ScheduledTaskData( schedulerTask );
			scheduledTaskData.setScheduled( scheduledTasksExecutionService.isTaskScheduled( schedulerTaskId ) );
			scheduledTaskDataMap.add( scheduledTaskData );
		}
		model.setScheduledTasksData( scheduledTaskDataMap );
	}

	private String getForwardToListView() {
		return String.format( "redirect:%s", urlUtilsService.getAdminSchedulerTaskListLink() );
	}
}
