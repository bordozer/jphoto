package rest.admin.restriction;

public class UserRestrictionHistoryEntryDTO {

	private int id;

	private String timeFrom;
	private String timeTo;

	private String restrictionName;

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

	@Override
	public String toString() {
		return String.format( "#%d %s: from %s to %s", id, restrictionName, timeFrom, timeTo );
	}
}
