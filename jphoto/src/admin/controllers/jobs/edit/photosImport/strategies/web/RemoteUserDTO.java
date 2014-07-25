package admin.controllers.jobs.edit.photosImport.strategies.web;

import core.enums.UserGender;
import core.general.user.UserMembershipType;

public class RemoteUserDTO {

	private final String remoteUserId;
	private String remoteUserName;
	private String remoteUserCardUrl;

	private boolean remoteUserFound;

	private boolean remoteUserExistsInTheSystem;
	private String userCardLink;
	private int photosCount;
	private String userPhotosUrl;
	private int remoteUserPhotosCount;

	private UserGender userGender;
	private UserMembershipType userMembershipType;

	public RemoteUserDTO( final String remoteUserId ) {
		this.remoteUserId = remoteUserId;
	}

	public String getRemoteUserId() {
		return remoteUserId;
	}

	public String getRemoteUserName() {
		return remoteUserName;
	}

	public void setRemoteUserName( final String remoteUserName ) {
		this.remoteUserName = remoteUserName;
	}

	public String getRemoteUserCardUrl() {
		return remoteUserCardUrl;
	}

	public void setRemoteUserCardUrl( final String remoteUserCardUrl ) {
		this.remoteUserCardUrl = remoteUserCardUrl;
	}

	public boolean isRemoteUserFound() {
		return remoteUserFound;
	}

	public void setRemoteUserFound( final boolean remoteUserFound ) {
		this.remoteUserFound = remoteUserFound;
	}

	@Override
	public String toString() {
		return String.format( "%s", remoteUserName );
	}

	public boolean isRemoteUserExistsInTheSystem() {
		return remoteUserExistsInTheSystem;
	}

	public void setRemoteUserExistsInTheSystem( final boolean remoteUserExistsInTheSystem ) {
		this.remoteUserExistsInTheSystem = remoteUserExistsInTheSystem;
	}

	public void setUserCardLink( final String userCardLink ) {
		this.userCardLink = userCardLink;
	}

	public String getUserCardLink() {
		return userCardLink;
	}

	public int getPhotosCount() {
		return photosCount;
	}

	public void setPhotosCount( final int photosCount ) {
		this.photosCount = photosCount;
	}

	public void setUserPhotosUrl( final String userPhotosUrl ) {
		this.userPhotosUrl = userPhotosUrl;
	}

	public String getUserPhotosUrl() {
		return userPhotosUrl;
	}

	public void setRemoteUserPhotosCount( final int remoteUserPhotosCount ) {
		this.remoteUserPhotosCount = remoteUserPhotosCount;
	}

	public int getRemoteUserPhotosCount() {
		return remoteUserPhotosCount;
	}

	public UserGender getUserGender() {
		return userGender;
	}

	public void setUserGender( final UserGender userGender ) {
		this.userGender = userGender;
	}

	public UserMembershipType getUserMembershipType() {
		return userMembershipType;
	}

	public void setUserMembershipType( final UserMembershipType userMembershipType ) {
		this.userMembershipType = userMembershipType;
	}
}
