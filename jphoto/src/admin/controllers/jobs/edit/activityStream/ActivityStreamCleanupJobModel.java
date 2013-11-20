package admin.controllers.jobs.edit.activityStream;

import admin.controllers.jobs.edit.AbstractAdminJobModel;

public class ActivityStreamCleanupJobModel extends AbstractAdminJobModel {

	public static final String LEAVE_ACTIVITY_FOR_DAYS_CONTROL = "leaveActivityForDays";

	private String leaveActivityForDays;

	public String getLeaveActivityForDays() {
		return leaveActivityForDays;
	}

	public void setLeaveActivityForDays( final String leaveActivityForDays ) {
		this.leaveActivityForDays = leaveActivityForDays;
	}
}
