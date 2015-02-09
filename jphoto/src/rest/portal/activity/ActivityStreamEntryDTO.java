package rest.portal.activity;

public class ActivityStreamEntryDTO {

	private int id;
	private String activityIcon;
	private String activityTime;
	private String activityUserLink;
	private String activityText;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public void setActivityText( final String activityText ) {
		this.activityText = activityText;
	}

	public String getActivityText() {
		return activityText;
	}

	public void setActivityTime( final String activityTime ) {
		this.activityTime = activityTime;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityUserLink( final String activityUserLink ) {
		this.activityUserLink = activityUserLink;
	}

	public String getActivityUserLink() {
		return activityUserLink;
	}

	public void setActivityIcon( final String activityIcon ) {
		this.activityIcon = activityIcon;
	}

	public String getActivityIcon() {
		return activityIcon;
	}
}
