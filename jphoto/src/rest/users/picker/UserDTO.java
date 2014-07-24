package rest.users.picker;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserDTO {

	private String userId;
	private String userName;
	private String userNameEscaped;
	private String userCardLink;
	private String userCardLinkList;
	private String userAvatarUrl;
	private String userGender;
	private String userGenderIcon;

	public String getUserId() {
		return userId;
	}

	public void setUserId( final String userId ) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public String getUserNameEscaped() {
		return userNameEscaped;
	}

	public void setUserNameEscaped( final String userNameEscaped ) {
		this.userNameEscaped = userNameEscaped;
	}

	public String getUserCardLink() {
		return userCardLink;
	}

	public void setUserCardLink( final String userCardLink ) {
		this.userCardLink = userCardLink;
	}

	public String getUserCardLinkList() {
		return userCardLinkList;
	}

	public void setUserCardLinkList( final String userCardLinkList ) {
		this.userCardLinkList = userCardLinkList;
	}

	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}

	public void setUserAvatarUrl( final String userAvatarUrl ) {
		this.userAvatarUrl = userAvatarUrl;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender( final String userGender ) {
		this.userGender = userGender;
	}

	public void setUserGenderIcon( final String userGenderIcon ) {
		this.userGenderIcon = userGenderIcon;
	}

	public String getUserGenderIcon() {
		return userGenderIcon;
	}
}
