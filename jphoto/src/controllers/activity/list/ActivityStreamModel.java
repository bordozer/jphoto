package controllers.activity.list;

import core.general.activity.AbstractActivityStreamEntry;
import core.general.base.AbstractGeneralPageModel;

import java.util.List;

public class ActivityStreamModel extends AbstractGeneralPageModel {

	private List<AbstractActivityStreamEntry> activities;

	public List<AbstractActivityStreamEntry> getActivities() {
		return activities;
	}

	public void setActivities( final List<AbstractActivityStreamEntry> activities ) {
		this.activities = activities;
	}
}
