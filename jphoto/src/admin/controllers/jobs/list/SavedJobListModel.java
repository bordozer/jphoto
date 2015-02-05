package admin.controllers.jobs.list;

import admin.jobs.entries.AbstractJob;
import admin.jobs.enums.JobListTab;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import admin.jobs.general.TabJobInfo;
import core.general.base.AbstractGeneralModel;
import core.general.scheduler.SchedulerTask;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavedJobListModel extends AbstractGeneralModel {

	private List<SavedJob> savedJobs;
	private Set<Integer> notDeletableJobIds;

	private Map<Integer, JobHistoryEntryDTO> activeJobHistoryMap;
	private Set<Integer> activeJobTypes;
	private Set<Integer> activeSavedJobIds;

	private List<AbstractJob> activeJobs;

	private JobListTab jobListTab;
	private SavedJobType savedJobType;
	private Map<JobListTab, TabJobInfo> tabJobInfosMap;

	private List<JobExecutionHistoryData> jobExecutionHistoryDatas;
	private List<String> selectedJobsIds;

	private int jobExecutionStatusIdFilter;
	private int jobTypeIdFilter;

	private List<SchedulerTask> schedulerTasks;
	private int schedulerTaskId;

	private String formAction;

	public List<SavedJob> getSavedJobs() {
		return savedJobs;
	}

	public void setSavedJobs( final List<SavedJob> savedJobs ) {
		this.savedJobs = savedJobs;
	}

	public Set<Integer> getNotDeletableJobIds() {
		return notDeletableJobIds;
	}

	public void setNotDeletableJobIds( final Set<Integer> notDeletableJobIds ) {
		this.notDeletableJobIds = notDeletableJobIds;
	}

	public Map<Integer, JobHistoryEntryDTO> getActiveJobHistoryMap() {
		return activeJobHistoryMap;
	}

	public void setActiveJobHistoryMap( final Map<Integer, JobHistoryEntryDTO> activeJobHistoryMap ) {
		this.activeJobHistoryMap = activeJobHistoryMap;
	}

	public Set<Integer> getActiveJobTypes() {
		return activeJobTypes;
	}

	public void setActiveJobTypes( final Set<Integer> activeJobTypes ) {
		this.activeJobTypes = activeJobTypes;
	}

	public Set<Integer> getActiveSavedJobIds() {
		return activeSavedJobIds;
	}

	public void setActiveSavedJobIds( final Set<Integer> activeSavedJobIds ) {
		this.activeSavedJobIds = activeSavedJobIds;
	}

	public List<AbstractJob> getActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs( final List<AbstractJob> activeJobs ) {
		this.activeJobs = activeJobs;
	}

	public JobListTab getJobListTab() {
		return jobListTab;
	}

	public void setJobListTab( final JobListTab jobListTab ) {
		this.jobListTab = jobListTab;
	}

	public SavedJobType getSavedJobType() {
		return savedJobType;
	}

	public void setSavedJobType( final SavedJobType savedJobType ) {
		this.savedJobType = savedJobType;
	}

	public Map<JobListTab, TabJobInfo> getTabJobInfosMap() {
		return tabJobInfosMap;
	}

	public void setTabJobInfosMap( final Map<JobListTab, TabJobInfo> tabJobInfosMap ) {
		this.tabJobInfosMap = tabJobInfosMap;
	}

	public List<JobExecutionHistoryData> getJobExecutionHistoryDatas() {
		return jobExecutionHistoryDatas;
	}

	public void setJobExecutionHistoryDatas( final List<JobExecutionHistoryData> jobExecutionHistoryDatas ) {
		this.jobExecutionHistoryDatas = jobExecutionHistoryDatas;
	}

	public List<String> getSelectedJobsIds() {
		return selectedJobsIds;
	}

	public void setSelectedJobsIds( final List<String> selectedJobsIds ) {
		this.selectedJobsIds = selectedJobsIds;
	}

	public void setJobExecutionStatusIdFilter( final int jobExecutionStatusIdFilter ) {
		this.jobExecutionStatusIdFilter = jobExecutionStatusIdFilter;
	}

	public int getJobExecutionStatusIdFilter() {
		return jobExecutionStatusIdFilter;
	}

	public void setJobTypeIdFilter( final int jobTypeIdFilter ) {
		this.jobTypeIdFilter = jobTypeIdFilter;
	}

	public int getJobTypeIdFilter() {
		return jobTypeIdFilter;
	}

	public List<SchedulerTask> getSchedulerTasks() {
		return schedulerTasks;
	}

	public void setSchedulerTasks( final List<SchedulerTask> schedulerTasks ) {
		this.schedulerTasks = schedulerTasks;
	}

	public int getSchedulerTaskId() {
		return schedulerTaskId;
	}

	public void setSchedulerTaskId( final int schedulerTasksId ) {
		this.schedulerTaskId = schedulerTasksId;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction( final String formAction ) {
		this.formAction = formAction;
	}
}
