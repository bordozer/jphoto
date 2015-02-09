package rest.portal.activity;

import java.util.List;

public class ActivityStreamModel {

	private int id = 1;

	private List<ActivityStreamEntryDTO> activityStreamEntryDTOs;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public List<ActivityStreamEntryDTO> getActivityStreamEntryDTOs() {
		return activityStreamEntryDTOs;
	}

	public void setActivityStreamEntryDTOs( final List<ActivityStreamEntryDTO> activityStreamEntryDTOs ) {
		this.activityStreamEntryDTOs = activityStreamEntryDTOs;
	}
}
