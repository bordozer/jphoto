package rest.photo.upload.userTeam;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import ui.translatable.GenericTranslatableEntry;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO {

	private int userTeamMemberId;
	private String userTeamMemberName;
	private boolean checked;
	private String userTeamMemberCardUrl;
	private int teamMemberTypeId;

	private int teamMemberPhotosQty;
	private String teamMemberTypeName;
	private String siteMemberLink;

	private List<TeamMemberTypeDTO> userTeamMemberTypes;

	public UserTeamMemberDTO() {
	}

	public UserTeamMemberDTO( final int userTeamMemberId ) {
		this.userTeamMemberId = userTeamMemberId;
	}

	public int getUserTeamMemberId() {
		return userTeamMemberId;
	}

	public void setUserTeamMemberId( final int userTeamMemberId ) {
		this.userTeamMemberId = userTeamMemberId;
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

	public void setUserTeamMemberTypes( final List<TeamMemberTypeDTO> userTeamMemberTypes ) {
		this.userTeamMemberTypes = userTeamMemberTypes;
	}

	public List<TeamMemberTypeDTO> getUserTeamMemberTypes() {
		return userTeamMemberTypes;
	}

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", userTeamMemberId, userTeamMemberName );
	}
}
