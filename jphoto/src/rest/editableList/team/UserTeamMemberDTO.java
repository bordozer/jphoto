package rest.editableList.team;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO {

	private int entryId;
	private int userId;
	private String userTeamMemberName;
	private boolean checked;
	private String userTeamMemberCardUrl;
	private int teamMemberTypeId;

	private String userTeamMemberNameTitle;
	private int teamMemberPhotosQty;
	private String teamMemberTypeName;
	private String siteMemberLink;

	public UserTeamMemberDTO() {
	}

	public UserTeamMemberDTO( final int entryId ) {
		this.entryId = entryId;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId( final int entryId ) {
		this.entryId = entryId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( int userId ) {
		this.userId = userId;
	}

	public String getUserTeamMemberName() {
		return userTeamMemberName;
	}

	public void setUserTeamMemberName( final String userTeamMemberName ) {
		this.userTeamMemberName = userTeamMemberName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked( final boolean checked ) {
		this.checked = checked;
	}

	public void setTeamMemberTypeId( final int teamMemberTypeId ) {
		this.teamMemberTypeId = teamMemberTypeId;
	}

	public int getTeamMemberTypeId() {
		return teamMemberTypeId;
	}

	public void setUserTeamMemberCardUrl( final String userTeamMemberCardUrl ) {
		this.userTeamMemberCardUrl = userTeamMemberCardUrl;
	}

	public String getUserTeamMemberCardUrl() {
		return userTeamMemberCardUrl;
	}

	public String getUserTeamMemberNameTitle() {
		return userTeamMemberNameTitle;
	}

	public void setUserTeamMemberNameTitle( final String userTeamMemberNameTitle ) {
		this.userTeamMemberNameTitle = userTeamMemberNameTitle;
	}

	public void setTeamMemberPhotosQty( final int teamMemberPhotosQty ) {
		this.teamMemberPhotosQty = teamMemberPhotosQty;
	}

	public int getTeamMemberPhotosQty() {
		return teamMemberPhotosQty;
	}

	public void setTeamMemberTypeName( final String teamMemberTypeName ) {
		this.teamMemberTypeName = teamMemberTypeName;
	}

	public String getTeamMemberTypeName() {
		return teamMemberTypeName;
	}

	public void setSiteMemberLink( final String siteMemberLink ) {
		this.siteMemberLink = siteMemberLink;
	}

	public String getSiteMemberLink() {
		return siteMemberLink;
	}

	@Override
	public String toString() {
		return String.format( "User ID: %d, TEam member: %d '%s'", userId, entryId, userTeamMemberName );
	}
}
