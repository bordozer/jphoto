package rest.photo.upload.userTeam;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO {

	private int userTeamMemberId;
	private String userTeamMemberName;

	private boolean checked;

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

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", userTeamMemberId, userTeamMemberName );
	}
}
