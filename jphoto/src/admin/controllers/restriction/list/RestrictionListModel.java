package admin.controllers.restriction.list;

import core.general.base.AbstractGeneralPageModel;
import org.json.JSONArray;

public class RestrictionListModel extends AbstractGeneralPageModel {

	private JSONArray restrictionTypes;
	private JSONArray restrictionStatuses;

	public void setRestrictionTypes( final JSONArray restrictionTypes ) {
		this.restrictionTypes = restrictionTypes;
	}

	public JSONArray getRestrictionTypes() {
		return restrictionTypes;
	}

	public void setRestrictionStatuses( final JSONArray restrictionStatuses ) {
		this.restrictionStatuses = restrictionStatuses;
	}

	public JSONArray getRestrictionStatuses() {
		return restrictionStatuses;
	}
}
