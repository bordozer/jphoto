package rest.admin.restriction;

public class UserRestrictionHistoryEntryDTO {

	private int id;

	private String dateFrom;
	private String timeFrom;

	private String dateTo;
	private String timeTo;

	private String restrictionDuration;
	private String expiresAfter;

	private String restrictionName;
	private String restrictionIcon;

	private String creatorLink;
	private String creationDate;
	private String creationTime;

	private boolean active;
	private boolean finished;

	private String cancellerLink;
	private String cancellingTime;

	private String cssClass;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom( final String dateFrom ) {
		this.dateFrom = dateFrom;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom( final String timeFrom ) {
		this.timeFrom = timeFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo( final String dateTo ) {
		this.dateTo = dateTo;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo( final String timeTo ) {
		this.timeTo = timeTo;
	}

	public String getRestrictionDuration() {
		return restrictionDuration;
	}

	public void setRestrictionDuration( final String restrictionDuration ) {
		this.restrictionDuration = restrictionDuration;
	}

	public String getExpiresAfter() {
		return expiresAfter;
	}

	public void setExpiresAfter( final String expiresAfter ) {
		this.expiresAfter = expiresAfter;
	}

	public String getRestrictionName() {
		return restrictionName;
	}

	public void setRestrictionName( final String restrictionName ) {
		this.restrictionName = restrictionName;
	}

	public String getRestrictionIcon() {
		return restrictionIcon;
	}

	public void setRestrictionIcon( final String restrictionIcon ) {
		this.restrictionIcon = restrictionIcon;
	}

	public String getCreatorLink() {
		return creatorLink;
	}

	public void setCreatorLink( final String creatorLink ) {
		this.creatorLink = creatorLink;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate( final String creationDate ) {
		this.creationDate = creationDate;
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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
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

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass( final String cssClass ) {
		this.cssClass = cssClass;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s: from %s to %s", id, restrictionName, timeFrom, timeTo );
	}
}
