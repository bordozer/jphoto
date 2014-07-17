package rest.photo.upload.userTeam;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO {

	private int userTeamMemberId;
	private String userTeamMemberName;

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

	@Override
	public String toString() {
		return String.format( "#%d: '%s'", userTeamMemberId, userTeamMemberName );
	}
}
