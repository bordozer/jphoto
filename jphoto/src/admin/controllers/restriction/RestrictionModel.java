package admin.controllers.restriction;

import org.json.JSONArray;

public class RestrictionModel {

	private int entryId;
	private String entryName;
	private JSONArray restrictionTypes;

	public void setEntryId( final int entryId ) {
		this.entryId = entryId;
	}

	public int getEntryId() {
		return entryId;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName( final String entryName ) {
		this.entryName = entryName;
	}

	public void setRestrictionTypes( final JSONArray restrictionTypes ) {
		this.restrictionTypes = restrictionTypes;
	}

	public JSONArray getRestrictionTypes() {
		return restrictionTypes;
	}
}
