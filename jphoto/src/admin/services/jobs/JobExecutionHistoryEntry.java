package admin.services.jobs;

import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import core.enums.SavedJobParameterKey;
import core.general.base.AbstractBaseEntity;
import core.general.base.CommonProperty;
import core.services.utils.DateUtilsService;
import ui.context.ApplicationContextHelper;

import java.util.Date;
import java.util.Map;

public class JobExecutionHistoryEntry extends AbstractBaseEntity {

	private Date startTime;
	private Date endTime;
	private String parametersDescription;
	private SavedJobType savedJobType;
	private JobExecutionStatus jobExecutionStatus;
	private String jobMessage;
	private SavedJob savedJob;
	private Map<SavedJobParameterKey, CommonProperty> parametersMap;

	private int currentJobStep;
	private int totalJobSteps;
	private String jobParametersDescription;

	private int scheduledTaskId;

	public Date getExecutionDuration() {
		final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();
		return dateUtilsService.getTimeBetween( startTime, dateUtilsService.getCurrentTime() );
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime( final Date startTime ) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime( final Date endTime ) {
		this.endTime = endTime;
	}

	public String getParametersDescription() {
		return parametersDescription;
	}

	public void setParametersDescription( final String parametersDescription ) {
		this.parametersDescription = parametersDescription;
	}

	public SavedJobType getSavedJobType() {
		return savedJobType;
	}

	public void setSavedJobType( final SavedJobType savedJobType ) {
		this.savedJobType = savedJobType;
	}

	public JobExecutionStatus getJobExecutionStatus() {
		return jobExecutionStatus;
	}

	public void setJobExecutionStatus( final JobExecutionStatus jobExecutionStatus ) {
		this.jobExecutionStatus = jobExecutionStatus;
	}

	public String getJobMessage() {
		return jobMessage;
	}

	public void setJobMessage( final String jobMessage ) {
		this.jobMessage = jobMessage;
	}

	public SavedJob getSavedJob() {
		return savedJob;
	}

	public void setSavedJob( final SavedJob savedJob ) {
		this.savedJob = savedJob;
	}

	public void setParametersMap( final Map<SavedJobParameterKey,CommonProperty> parametersMap ) {
		this.parametersMap = parametersMap;
	}

	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		return parametersMap;
	}

	public int getCurrentJobStep() {
		return currentJobStep;
	}

	public void setCurrentJobStep( final int currentJobStep ) {
		this.currentJobStep = currentJobStep;
	}

	public int getTotalJobSteps() {
		return totalJobSteps;
	}

	public void setTotalJobSteps( final int totalJobSteps ) {
		this.totalJobSteps = totalJobSteps;
	}

	public void setJobParametersDescription( final String jobParametersDescription ) {
		this.jobParametersDescription = jobParametersDescription;
	}

	public String getJobParametersDescription() {
		return jobParametersDescription;
	}

	public boolean isChainJob() {
		return savedJobType == SavedJobType.JOB_CHAIN;
	}

	public int getScheduledTaskId() {
		return scheduledTaskId;
	}

	public void setScheduledTaskId( final int scheduledTaskId ) {
		this.scheduledTaskId = scheduledTaskId;
	}
}
