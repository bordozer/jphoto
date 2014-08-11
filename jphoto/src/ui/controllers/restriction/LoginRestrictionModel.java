package ui.controllers.restriction;

import core.general.base.AbstractGeneralPageModel;
import core.general.restriction.EntryRestriction;

public class LoginRestrictionModel extends AbstractGeneralPageModel {

	private final int restrictionEntryId;
	private EntryRestriction restriction;

	private String restrictionDateFrom;
	private String restrictionTimeFrom;
	private String restrictionDateTo;
	private String restrictionTimeTo;

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

	public void setRestrictionDateFrom( final String restrictionDateFrom ) {
		this.restrictionDateFrom = restrictionDateFrom;
	}

	public String getRestrictionDateFrom() {
		return restrictionDateFrom;
	}

	public void setRestrictionTimeFrom( final String restrictionTimeFrom ) {
		this.restrictionTimeFrom = restrictionTimeFrom;
	}

	public String getRestrictionTimeFrom() {
		return restrictionTimeFrom;
	}

	public void setRestrictionDateTo( final String restrictionDateTo ) {
		this.restrictionDateTo = restrictionDateTo;
	}

	public String getRestrictionDateTo() {
		return restrictionDateTo;
	}

	public void setRestrictionTimeTo( final String restrictionTimeTo ) {
		this.restrictionTimeTo = restrictionTimeTo;
	}

	public String getRestrictionTimeTo() {
		return restrictionTimeTo;
	}
}
