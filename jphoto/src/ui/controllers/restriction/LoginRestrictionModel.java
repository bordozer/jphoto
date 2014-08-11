package ui.controllers.restriction;

import core.general.base.AbstractGeneralPageModel;
import core.general.restriction.EntryRestriction;

public class LoginRestrictionModel extends AbstractGeneralPageModel {

	private final int restrictionEntryId;
	private EntryRestriction restriction;

	public LoginRestrictionModel( final int restrictionEntryId ) {
		this.restrictionEntryId = restrictionEntryId;
	}

	public int getRestrictionEntryId() {
		return restrictionEntryId;
	}

	public void setRestriction( final EntryRestriction restriction ) {
		this.restriction = restriction;
	}

	public EntryRestriction getRestriction() {
		return restriction;
	}
}
