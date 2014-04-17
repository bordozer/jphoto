package core.general.restriction;

import core.enums.RestrictionType;
import core.general.base.AbstractBaseEntity;
import core.interfaces.Restrictable;

import java.util.Date;

public class EntryRestriction<T extends Restrictable> extends AbstractBaseEntity {

	private final T entry;
	private final RestrictionType restrictionType;

	private Date restrictionTimeFrom;
	private Date restrictionTimeTo;

	private String restrictionMessage;
	private String restrictionRestrictionComment;

	public EntryRestriction( final T entry, final RestrictionType restrictionType ) {
		this.entry = entry;
		this.restrictionType = restrictionType;
	}

	public T getEntry() {
		return entry;
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
