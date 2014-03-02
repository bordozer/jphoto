package admin.controllers.scheduler.tasks.list;

import admin.jobs.general.SavedJob;
import admin.services.jobs.SavedJobService;
import admin.services.scheduler.ScheduledTasksExecutionService;
import admin.services.scheduler.SchedulerService;
import core.general.scheduler.SchedulerTask;
import core.log.LogHelper;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.utils.UrlUtilsService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.ListUtils;
import utils.TranslatorUtils;

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
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private ScheduledTasksExecutionService scheduledTasksExecutionService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	private final LogHelper log = new LogHelper( SchedulerTaskListController.class );

	@ModelAttribute( MODEL_NAME )
	public SchedulerTaskListModel prepareModel() throws SchedulerException {
		final SchedulerTaskListModel model = new SchedulerTaskListModel();

		model.setPageTitleData( pageTitleAdminUtilsService.getAdminSchedulerTaskListData() );

		setScheduledTaskData( model );

		final Map<Integer, SavedJob> map = newLinkedHashMap();
		final List<SavedJob> savedJobs = savedJobService.loadAll();
		for ( final SavedJob savedJob : savedJobs ) {
			map.put( savedJob.getId(), savedJob );
		}
		model.setSavedJobMap( map );

		model.setSchedulerRunning( scheduledTasksExecutionService.isRunning() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/tasks/" )
	public String schedulerTaskList( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {
		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/run/" )
	public String schedulerStart( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {
		try {
			log.info( "Starting scheduler" );
			scheduledTasksExecutionService.start();
		} catch ( SchedulerException e ) {
			model.getBindingResult().reject( TranslatorUtils.translate( "Scheduler error" ), TranslatorUtils.translate( "Can not start Scheduler" ) );
			log.error( "Can not start Scheduler" );
		}

		return getForwardToListView();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/" )
	public String schedulerStop( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) {
		try {
			log.info( "Stopping scheduler" );
			scheduledTasksExecutionService.pauseAll();
		} catch ( SchedulerException e ) {
			model.getBindingResult().reject( TranslatorUtils.translate( "Scheduler error" ), TranslatorUtils.translate( "Can not stop Scheduler" ) );
			log.error( "Can not stop Scheduler" );
		}

		return getForwardToListView();
	}

	@RequestMapping( method = RequestMethod.POST, value = "/tasks/" )
	public String schedulerTaskActivate( final @ModelAttribute( MODEL_NAME ) SchedulerTaskListModel model ) throws SchedulerException {

		final String formAction = model.getSchedulerTasksFormAction();
		final List<Integer> ids = ListUtils.convertStringListToInteger( Arrays.asList( model.getSchedulerTaskCheckbox() ) );

		switch ( formAction ) {
			case SCHEDULER_TASK_LIST_GROUP_ACTION_ACTIVATE:
				schedulerService.activateSchedulerTasks( ids );
				break;
			case SCHEDULER_TASK_LIST_GROUP_ACTION_DEACTIVATE:
				schedulerService.deactivateSchedulerTasks( ids );
				break;
			case SCHEDULER_TASK_LIST_GROUP_ACTION_DELETE:
				for ( final int id : ids ) {
					schedulerService.delete( id );
				}
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
