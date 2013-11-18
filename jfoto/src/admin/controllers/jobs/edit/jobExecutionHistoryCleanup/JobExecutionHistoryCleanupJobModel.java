package admin.controllers.jobs.edit.jobExecutionHistoryCleanup;

import admin.controllers.jobs.edit.AbstractAdminJobModel;

import java.util.List;

public class JobExecutionHistoryCleanupJobModel extends AbstractAdminJobModel {

	public static final String DELETE_ENTRIES_OLDER_THE_N_DAYS_CONTROL = "deleteEntriesOlderThenDays";
	public static final String JOB_EXECUTION_STATUS_IDS_TO_DELETE_CONTROL = "jobExecutionStatusIdsToDelete";

	private String deleteEntriesOlderThenDays;
	private List<String> jobExecutionStatusIdsToDelete;

	public String getDeleteEntriesOlderThenDays() {
		return deleteEntriesOlderThenDays;
	}

	public void setDeleteEntriesOlderThenDays( final String deleteEntriesOlderThenDays ) {
		this.deleteEntriesOlderThenDays = deleteEntriesOlderThenDays;
	}

	public List<String> getJobExecutionStatusIdsToDelete() {
		return jobExecutionStatusIdsToDelete;
	}

	public void setJobExecutionStatusIdsToDelete( final List<String> jobExecutionStatusIdsToDelete ) {
		this.jobExecutionStatusIdsToDelete = jobExecutionStatusIdsToDelete;
	}
}
