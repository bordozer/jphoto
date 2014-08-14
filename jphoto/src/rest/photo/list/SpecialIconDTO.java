package rest.photo.list;

public class SpecialIconDTO {

	private String icon;
	private String restrictionTypeName;
	private String restrictionMessage;

	public String getIcon() {
		return icon;
	}

	public void setIcon( final String icon ) {
		this.icon = icon;
	}

	public String getRestrictionTypeName() {
		return restrictionTypeName;
	}

	public void setRestrictionTypeName( final String restrictionTypeName ) {
		this.restrictionTypeName = restrictionTypeName;
	}

	public String getRestrictionMessage() {
		return restrictionMessage;
	}

	public void setRestrictionMessage( final String restrictionMessage ) {
		this.restrictionMessage = restrictionMessage;
	}
}
