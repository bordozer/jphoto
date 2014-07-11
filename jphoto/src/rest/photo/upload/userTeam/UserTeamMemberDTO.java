package rest.photo.upload.userTeam;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserTeamMemberDTO {

	private int userTeamMemberId;

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
}
