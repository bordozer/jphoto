package core.general.restriction;

import core.enums.RestrictionType;
import core.general.base.AbstractBaseEntity;

import java.util.Date;

public class EntryRestriction extends AbstractBaseEntity {

	private final int entryId;
	private final RestrictionType restrictionType;

	private Date restrictionTimeFrom;
	private Date restrictionTimeTo;

	private String restrictionMessage;
	private String restrictionRestrictionComment;

	public EntryRestriction( final int entryId, final RestrictionType restrictionType ) {
		this.entryId = entryId;
		this.restrictionType = restrictionType;
	}

	public int getEntryId() {
		return entryId;
	}

	public RestrictionType getRestrictionType() {
		return restrictionType;
	}

	public Date getRestrictionTimeFrom() {
		return restrictionTimeFrom;
	}

	public void setRestrictionTimeFrom( final Date restrictionTimeFrom ) {
		this.restrictionTimeFrom = restrictionTimeFrom;
	}

	public Date getRestrictionTimeTo() {
		return restrictionTimeTo;
	}

	public void setRestrictionTimeTo( final Date restrictionTimeTo ) {
		this.restrictionTimeTo = restrictionTimeTo;
	}

	public String getRestrictionMessage() {
		return restrictionMessage;
	}

	public void setRestrictionMessage( final String restrictionMessage ) {
		this.restrictionMessage = restrictionMessage;
	}

	public String getRestrictionRestrictionComment() {
		return restrictionRestrictionComment;
	}

	public void setRestrictionRestrictionComment( final String restrictionRestrictionComment ) {
		this.restrictionRestrictionComment = restrictionRestrictionComment;
	}
}
