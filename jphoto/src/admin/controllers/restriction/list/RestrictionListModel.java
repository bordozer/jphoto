package admin.controllers.restriction.list;

import core.general.base.AbstractGeneralPageModel;
import org.json.JSONArray;

public class RestrictionListModel extends AbstractGeneralPageModel {

	private JSONArray restrictionTypesUser;
	private JSONArray restrictionTypesPhoto;
	private JSONArray restrictionStatuses;

	public JSONArray getRestrictionTypesUser() {
		return restrictionTypesUser;
	}

	public void setRestrictionTypesUser( final JSONArray restrictionTypesUser ) {
		this.restrictionTypesUser = restrictionTypesUser;
	}

	public JSONArray getRestrictionTypesPhoto() {
		return restrictionTypesPhoto;
	}

	public void setRestrictionTypesPhoto( final JSONArray restrictionTypesPhoto ) {
		this.restrictionTypesPhoto = restrictionTypesPhoto;
	}

	public void setRestrictionStatuses( final JSONArray restrictionStatuses ) {
		this.restrictionStatuses = restrictionStatuses;
	}

	public JSONArray getRestrictionStatuses() {
		return restrictionStatuses;
	}
}
