package admin.controllers.jobs.list;

import admin.jobs.entries.AbstractJob;
import admin.jobs.enums.JobListTab;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import admin.jobs.loaders.AbstractSavedJobsLoader;
import admin.jobs.loaders.SavedJobLoaderFactory;
import admin.services.jobs.*;
import admin.services.scheduler.SchedulerService;
import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import utils.ListUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;

@Controller
@RequestMapping( "jobs" )
public class SavedJobListController {

	public static final int JOB_PROGRESS_INTERVAL = 2000;

	private final static String JOB_TEMPLATE_VIEW = "admin/jobs/list/JobTemplateList";
	private final static String SAVED_JOBS_VIEW = "admin/jobs/list/SavedJobList";
	private final static String DONE_JOBS_VIEW = "admin/jobs/list/JobExecutionHistory";

	private static final String MODEL_NAME = "savedJobListModel";

	@Autowired
	private SavedJobService savedJobService;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Autowired
	private JobExecutionHistoryService jobExecutionHistoryService;
	
	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	public SavedJobListModel prepareModel() {
		return new SavedJobListModel();
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( configurationService.getInt( ConfigurationKey.ADMIN_JOB_HISTORY_ITEMS_ON_PAGE ) );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String jobList( final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {
		return String.format( "forward:%s/", JobListTab.TEMPLATES.getKey() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/templates/" )
	public String jobTemplateList( final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {

		setActiveJobMaps( model );

		setActiveJobs( model );

		final SavedJobLoaderFactory loaderFactory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );
		model.setTabJobInfosMap( loaderFactory.getTabJobInfos() );

		model.setJobListTab( JobListTab.TEMPLATES );
		model.setPageTitleData( breadcrumbsAdminService.getJobListBreadcrumbs( JobListTab.TEMPLATES ) );

		return JOB_TEMPLATE_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{jobListTabKey}/" )
	public String savedJobByTabList( final @PathVariable( "jobListTabKey" ) String jobListTabKey, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {

		final SavedJobLoaderFactory loaderFactory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );

		final JobListTab jobListTab = JobListTab.getByKey( jobListTabKey );
		final AbstractSavedJobsLoader jobsLoader = loaderFactory.getInstance( jobListTab );
		final List<SavedJob> savedJobs = jobsLoader.load();

		prepareJobs( model, jobListTab, savedJobs );

		model.setPageTitleData( breadcrumbsAdminService.getJobListBreadcrumbs( jobListTab ) );

		return SAVED_JOBS_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{jobListTabKey}/{jobTypeId}" )
	public String savedJobByTypeList( final @PathVariable( "jobListTabKey" ) String jobListTabKey, final @PathVariable( "jobTypeId" ) int jobTypeId, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {

		final SavedJobLoaderFactory loaderFactory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );

		final JobListTab jobListTab = JobListTab.getByKey( jobListTabKey );
		final SavedJobType savedJobType = SavedJobType.getById( jobTypeId );

		final AbstractSavedJobsLoader jobsLoader = loaderFactory.getInstance( jobListTab );
		final List<SavedJob> savedJobs = jobsLoader.load( savedJobType );

		model.setSavedJobType( savedJobType );

		prepareJobs( model, jobListTab, savedJobs );

		model.setPageTitleData( breadcrumbsAdminService.getSavedJobListFilteredByJobTypeBreadcrumbs( jobListTab, savedJobType ) );

		return SAVED_JOBS_VIEW;
	}

	@RequestMapping( value = "/done/" )
	public String doneJobListAll( final @ModelAttribute( MODEL_NAME ) SavedJobListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return showJobHistoryList( 0, 0, model, pagingModel );
	}

	@RequestMapping( value = "/done/status/{jobExecutionStatusId}/" )
	public String doneJobListByStatus( final @PathVariable( "jobExecutionStatusId" ) int jobExecutionStatusId, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return showJobHistoryList( jobExecutionStatusId, 0, model, pagingModel );
	}

	@RequestMapping( value = "/done/type/{jobTypeId}/" )
	public String doneJobListByJobType( final @PathVariable( "jobTypeId" ) int jobTypeId, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return showJobHistoryList( 0, jobTypeId, model, pagingModel );
	}

	@RequestMapping( value = "/done/status/{jobExecutionStatusId}/type/{jobTypeId}/" )
	public String doneJobListByStatusAndJobType( final @PathVariable( "jobExecutionStatusId" ) int jobExecutionStatusId, final @PathVariable( "jobTypeId" ) int jobTypeId
		, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		return showJobHistoryList( jobExecutionStatusId, jobTypeId, model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/delete/{jobExecutionHistoryEntryId}/" )
	public String deleteJobExecutionHistoryEntry( final @PathVariable( "jobExecutionHistoryEntryId" ) int jobExecutionHistoryEntryId, final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {
		jobExecutionHistoryService.delete( jobExecutionHistoryEntryId );
		return String.format( "redirect:%s/jobs/done/", urlUtilsService.getBaseAdminURL() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/delete/" )
	public String deleteJobExecutionHistories( final @ModelAttribute( MODEL_NAME ) SavedJobListModel model ) {

		final List<String> selectedJobsIds = model.getSelectedJobsIds();
		if ( selectedJobsIds != null && ! selectedJobsIds.isEmpty() ) {
			final List<Integer> jobIdsToDelete = ListUtils.convertStringListToInteger( selectedJobsIds );
			for ( final int jobId : jobIdsToDelete ) {
				jobExecutionHistoryService.delete( jobId );
			}
		}

		return String.format( "redirect:%s/jobs/done/", urlUtilsService.getBaseAdminURL() );
	}

	private String showJobHistoryList( final int jobExecutionStatusId, final int jobTypeId, final SavedJobListModel model, final PagingModel pagingModel ) {
		setActiveJobs( model );

		final SqlTable jobHistoryTable = new SqlTable( JobExecutionHistoryDaoImpl.TABLE_JOB_EXECUTION_HISTORY );
		final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery( jobHistoryTable );

		if ( jobExecutionStatusId > 0 ) {
			final SqlColumnSelectable jobStatusCol = new SqlColumnSelect( jobHistoryTable, JobExecutionHistoryDaoImpl.TABLE_JOB_DONE_COL_JOB_STATUS_ID );
			final SqlCondition condition = new SqlCondition( jobStatusCol, SqlCriteriaOperator.EQUALS, jobExecutionStatusId, dateUtilsService );
			selectQuery.addWhereAnd( condition );
		}
		if ( jobTypeId > 0 ) {
			final SqlColumnSelectable tSavedJobColJobType = new SqlColumnSelect( jobHistoryTable, JobExecutionHistoryDaoImpl.TABLE_JOB_DONE_COL_JOB_TYPE_ID );
			final SqlCondition condition = new SqlCondition( tSavedJobColJobType, SqlCriteriaOperator.EQUALS, jobTypeId, dateUtilsService );
			selectQuery.addWhereAnd( condition );
		}
		if ( model.getSchedulerTaskId() > 0 ) {
			final SqlColumnSelectable tSavedJobColSchedulerTask = new SqlColumnSelect( jobHistoryTable, JobExecutionHistoryDaoImpl.TABLE_JOB_DONE_COL_SCHEDULER_TASK_ID );
			final SqlCondition condition = new SqlCondition( tSavedJobColSchedulerTask, SqlCriteriaOperator.EQUALS, model.getSchedulerTaskId(), dateUtilsService );
			selectQuery.addWhereAnd( condition );
		}
		final SqlColumnSelectable sortColumn = new SqlColumnSelect( jobHistoryTable, JobExecutionHistoryDaoImpl.ENTITY_ID );
		selectQuery.addSortingDesc( sortColumn );
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );

		final SqlSelectIdsResult idsResult = jobExecutionHistoryService.load( selectQuery );

		model.setJobExecutionHistoryDatas( getJobExecutionHistoryData( idsResult ) );

		model.setSchedulerTasks( schedulerService.loadAll() );

		setActiveJobMaps( model );

		final SavedJobLoaderFactory factory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );

		model.setTabJobInfosMap( factory.getTabJobInfos() );

		model.setJobListTab( JobListTab.JOB_EXECUTION_HISTORY );
		model.setPageTitleData( breadcrumbsAdminService.getJobListBreadcrumbs( JobListTab.JOB_EXECUTION_HISTORY ) );

		model.setJobExecutionStatusIdFilter( jobExecutionStatusId );
		model.setJobTypeIdFilter( jobTypeId );

		pagingModel.setTotalItems( idsResult.getRecordQty() );

		return DONE_JOBS_VIEW;
	}

	private List<JobExecutionHistoryData> getJobExecutionHistoryData( final SqlSelectIdsResult idsResult ) {
		final List<JobExecutionHistoryData> entryList = newArrayList();
		for ( final int id : idsResult.getIds() ) {
			final JobExecutionHistoryEntry jobExecutionHistoryEntry = jobExecutionHistoryService.load( id );
			final JobExecutionHistoryData jobExecutionHistoryData = new JobExecutionHistoryData( jobExecutionHistoryEntry );

			final int scheduledTaskId = jobExecutionHistoryEntry.getScheduledTaskId();
			if ( scheduledTaskId > 0 ) {
				jobExecutionHistoryData.setSchedulerTask( schedulerService.load( scheduledTaskId ) );
			}

			entryList.add( jobExecutionHistoryData );
		}
		return entryList;
	}

	private void prepareJobs( final SavedJobListModel model, final JobListTab jobListTab, final List<SavedJob> savedJobs ) {

		model.setSavedJobs( savedJobs );

		final SavedJobLoaderFactory factory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );
		model.setTabJobInfosMap( factory.getTabJobInfos() );

		model.setNotDeletableJobIds( savedJobService.getNotDeletableJobIds() );

		setActiveJobMaps( model );

		final List<AbstractJob> activeJobs = jobExecutionService.getActiveJobs();

		final Set<Integer> activeSavedJobIds = newHashSet();
		for ( final AbstractJob activeJob : activeJobs ) {
			activeSavedJobIds.add( activeJob.getSavedJobId() );
		}
		model.setActiveSavedJobIds( activeSavedJobIds );

		setActiveJobs( model );

		// set relation between saved job and execution job
		for ( final AbstractJob activeJob : activeJobs ) {
			for ( final SavedJob savedJob : savedJobs ) {
				if ( activeJob.getSavedJobId() == savedJob.getId() ) {
					savedJob.getJob().setJobId( activeJob.getJobId() );
				}
			}
		}

		model.setJobListTab( jobListTab );
	}

	private void setActiveJobs( final SavedJobListModel model ) {
		model.setActiveJobs( jobExecutionService.getActiveJobs() );
	}

	private void setActiveJobMaps( final SavedJobListModel model ) {

		final Map<Integer, JobHistoryEntryDTO> activeJobHistoryEntries = newLinkedHashMap();

		final List<JobExecutionHistoryEntry> historyEntriesOfActiveJobs = jobExecutionHistoryService.getActiveJobs();
		for ( final JobExecutionHistoryEntry entry : historyEntriesOfActiveJobs ) {
			final int percentage = entry.getCurrentJobStep() * 100 / entry.getTotalJobSteps();
			activeJobHistoryEntries.put( entry.getId(), new JobHistoryEntryDTO( entry.getId(), entry.getSavedJob().getJobType(), percentage ) );
		}
		model.setActiveJobHistoryMap( activeJobHistoryEntries );

		final Set<Integer> activeJobTypes = newHashSet();

		final List<AbstractJob> activeJobs = jobExecutionService.getActiveJobs();
		for ( final AbstractJob activeJob : activeJobs ) {
			activeJobTypes.add( activeJob.getJobType().getId() );
		}

		model.setActiveJobTypes( activeJobTypes );
	}

}
