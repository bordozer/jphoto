package admin.controllers.jobs.edit.photosImport.strategies.photosight;

public class PhotosightUserDTO {

	private final int photosightUserId;
	private String photosightUserName;
	private String photosightUserCardUrl;

	private boolean photosightUserFound;

	private boolean photosightUserExistsInTheSystem;
	private String userCardLink;
	private int photosCount;
	private String userPhotosUrl;

	public PhotosightUserDTO( final int photosightUserId ) {
		this.photosightUserId = photosightUserId;
	}

	public int getPhotosightUserId() {
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
}
