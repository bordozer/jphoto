package admin.controllers.jobs.edit;

import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobExecutionHistoryEntry;
import admin.jobs.enums.JobListTab;
import admin.jobs.general.TabJobInfo;
import core.general.base.AbstractGeneralModel;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractAdminJobModel extends AbstractGeneralModel {

	public final static String SAVE_JOB_MODE_FORM_CONTROL = "saveJobMode";
	public final static String SAVE_JOB_AS_COPY_FORM_CONTROL = "saveAsCopy";
	public final static String SAVE_JOB_NAME_FORM_CONTROL = "jobName";
	public final static String SAVE_JOB_ACTIVE_FORM_CONTROL = "active";

	protected AbstractJob job;

	private int savedJobId;
	private String jobName;
	private boolean active = true;

	protected int photosTotal;
	protected int usersTotal;

	protected boolean saveJobMode;
	protected boolean saveAsCopy;

	private JobListTab jobListTab;
	private Map<JobListTab, TabJobInfo> tabJobInfosMap;
	private List<AbstractJob> activeJobs;
	private String referrer;

	private JobExecutionHistoryEntry jobExecutionHistoryEntry;

	public AbstractJob getJob() {
		return job;
	}

	public void setJob( final AbstractJob job ) {
		this.job = job;
	}

	public int getSavedJobId() {
		return savedJobId;
	}

	public void setSavedJobId( final int savedJobId ) {
		this.savedJobId = savedJobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName( final String jobName ) {
		this.jobName = jobName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public boolean isSaveJobMode() {
		return saveJobMode;
	}

	public void setSaveJobMode( final boolean saveJobMode ) {
		this.saveJobMode = saveJobMode;
	}

	public boolean isSaveAsCopy() {
		return saveAsCopy;
	}

	public void setSaveAsCopy( final boolean saveAsCopy ) {
		this.saveAsCopy = saveAsCopy;
	}

	public int getPhotosTotal() {
		return photosTotal;
	}

	public void setPhotosTotal( final int photosTotal ) {
		this.photosTotal = photosTotal;
	}

	public int getUsersTotal() {
		return usersTotal;
	}

	public void setUsersTotal( final int usersTotal ) {
		this.usersTotal = usersTotal;
	}

	public JobListTab getJobListTab() {
		return jobListTab;
	}

	public void setJobListTab( final JobListTab jobListTab ) {
		this.jobListTab = jobListTab;
	}

	public Map<JobListTab, TabJobInfo> getTabJobInfosMap() {
		return tabJobInfosMap;
	}

	public void setTabJobInfosMap( final Map<JobListTab, TabJobInfo> tabJobInfosMap ) {
		this.tabJobInfosMap = tabJobInfosMap;
	}

	public List<AbstractJob> getActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs( final List<AbstractJob> activeJobs ) {
		this.activeJobs = activeJobs;
	}

	@Override
	public void clear() {
		super.clear();

		savedJobId = 0;
		jobName = null;
		active = false;
	}

	public void setReferrer( final String referrer ) {
		this.referrer = referrer;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setJobExecutionHistoryEntry( final JobExecutionHistoryEntry jobExecutionHistoryEntry ) {
		this.jobExecutionHistoryEntry = jobExecutionHistoryEntry;
	}

	public JobExecutionHistoryEntry getJobExecutionHistoryEntry() {
		return jobExecutionHistoryEntry;
	}
}
