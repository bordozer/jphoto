package admin.controllers.jobs.edit.activityStream;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import ui.activity.ActivityType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ActivityStreamCleanupJobModel extends AbstractAdminJobModel {

	public static final String LEAVE_ACTIVITY_FOR_DAYS_CONTROL = "leaveActivityForDays";
	public static final String ACTIVITY_STREAM_TYPE_IDS_TO_DELETE_CONTROL = "activityStreamTypeIdsToDelete";

	private String leaveActivityForDays;
	private List<String> activityStreamTypeIdsToDelete;

	public String getLeaveActivityForDays() {
		return leaveActivityForDays;
	}

	public void setLeaveActivityForDays( final String leaveActivityForDays ) {
		this.leaveActivityForDays = leaveActivityForDays;
	}

	public List<String> getActivityStreamTypeIdsToDelete() {
		return activityStreamTypeIdsToDelete;
	}

	public void setActivityStreamTypeIdsToDelete( final List<String> activityStreamTypeIdsToDelete ) {
		this.activityStreamTypeIdsToDelete = activityStreamTypeIdsToDelete;
	}

	public static String getLeaveActivityForDaysControl() {
		return LEAVE_ACTIVITY_FOR_DAYS_CONTROL;
	}

	@Override
	public void clear() {
		super.clear();

		activityStreamTypeIdsToDelete = newArrayList();
		for ( final ActivityType activityType : ActivityType.values() ) {
			activityStreamTypeIdsToDelete.add( String.valueOf( activityType.getId() ) );
		}
	}
}
