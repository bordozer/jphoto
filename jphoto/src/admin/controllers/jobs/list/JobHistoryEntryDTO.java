package admin.controllers.jobs.list;

import admin.jobs.enums.SavedJobType;

public class JobHistoryEntryDTO {

	private int jobHistoryEntryId;
	private SavedJobType jobType;
	private int percentage;
	private boolean savedJob;

	public JobHistoryEntryDTO() {
	}

	public JobHistoryEntryDTO( final int jobHistoryEntryId, final SavedJobType jobType, final int percentage ) {
		this.jobHistoryEntryId = jobHistoryEntryId;
		this.jobType = jobType;
		this.percentage = percentage;
	}

	public int getJobHistoryEntryId() {
		return jobHistoryEntryId;
	}

	public void setJobHistoryEntryId( final int jobHistoryEntryId ) {
		this.jobHistoryEntryId = jobHistoryEntryId;
	}

	public SavedJobType getJobType() {
		return jobType;
	}

	public void setJobType( final SavedJobType jobType ) {
		this.jobType = jobType;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage( final int percentage ) {
		this.percentage = percentage;
	}

	public boolean isSavedJob() {
		return savedJob;
	}

	public void setSavedJob( final boolean savedJob ) {
		this.savedJob = savedJob;
	}
}
