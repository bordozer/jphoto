package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import core.enums.UserGender;
import core.general.user.UserMembershipType;

public class PhotosightUserDTO {

	private final String photosightUserId;
	private String photosightUserName;
	private String photosightUserCardUrl;

	private boolean photosightUserFound;

	private boolean photosightUserExistsInTheSystem;
	private String userCardLink;
	private int photosCount;
	private String userPhotosUrl;
	private int photosightUserPhotosCount;

	private UserGender userGender;
	private UserMembershipType userMembershipType;

	public PhotosightUserDTO( final String photosightUserId ) {
		this.photosightUserId = photosightUserId;
	}

	public String getPhotosightUserId() {
		return photosightUserId;
	}

	public String getPhotosightUserName() {
		return photosightUserName;
	}

	public void setPhotosightUserName( final String photosightUserName ) {
		this.photosightUserName = photosightUserName;
	}

	public String getPhotosightUserCardUrl() {
		return photosightUserCardUrl;
	}

	public void setPhotosightUserCardUrl( final String photosightUserCardUrl ) {
		this.photosightUserCardUrl = photosightUserCardUrl;
	}

	public boolean isPhotosightUserFound() {
		return photosightUserFound;
	}

	public void setPhotosightUserFound( final boolean photosightUserFound ) {
		this.photosightUserFound = photosightUserFound;
	}

	@Override
	public String toString() {
		return String.format( "%s", photosightUserName );
	}

	public boolean isPhotosightUserExistsInTheSystem() {
		return photosightUserExistsInTheSystem;
	}

	public void setPhotosightUserExistsInTheSystem( final boolean photosightUserExistsInTheSystem ) {
		this.photosightUserExistsInTheSystem = photosightUserExistsInTheSystem;
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

	public void setPhotosightUserPhotosCount( final int photosightUserPhotosCount ) {
		this.photosightUserPhotosCount = photosightUserPhotosCount;
	}

	public int getPhotosightUserPhotosCount() {
		return photosightUserPhotosCount;
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
