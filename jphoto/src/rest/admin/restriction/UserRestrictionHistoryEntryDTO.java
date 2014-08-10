package rest.admin.restriction;

public class UserRestrictionHistoryEntryDTO {

	private int id;

	private String timeFrom;
	private String timeTo;

	private String restrictionName;

	private String creatorLink;
	private String creationTime;

	private boolean active;

	private String cancellerLink;
	private String cancellingTime;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom( final String timeFrom ) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo( final String timeTo ) {
		this.timeTo = timeTo;
	}

	public String getRestrictionName() {
		return restrictionName;
	}

	public void setRestrictionName( final String restrictionName ) {
		this.restrictionName = restrictionName;
	}

	public String getCreatorLink() {
		return creatorLink;
	}

	public void setCreatorLink( final String creatorLink ) {
		this.creatorLink = creatorLink;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime( final String creationTime ) {
		this.creationTime = creationTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public String getCancellerLink() {
		return cancellerLink;
	}

	public void setCancellerLink( final String cancellerLink ) {
		this.cancellerLink = cancellerLink;
	}

	public String getCancellingTime() {
		return cancellingTime;
	}

	public void setCancellingTime( final String cancellingTime ) {
		this.cancellingTime = cancellingTime;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s: from %s to %s", id, restrictionName, timeFrom, timeTo );
	}
}
