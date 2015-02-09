package rest.editableList.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rest.editableList.AbstractEditableListEntryDTO;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO extends AbstractEditableListEntryDTO {

	private boolean checked;
	private String userTeamMemberCardUrl;
	private int teamMemberTypeId;

	private String userTeamMemberNameTitle;
	private int teamMemberPhotosQty;
	private String teamMemberTypeName;
	private String siteMemberLink;

	public UserTeamMemberDTO() {
		super();
	}

	public UserTeamMemberDTO( final int entryId ) {
		super( entryId );
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
		return String.format( "User ID: %d, TEam member: %d '%s'", userId, entryId, entryName );
	}
}
