package admin.controllers.jobs.edit.photosImport.strategies.photosight;

public class PhotosightUserDTO {

	private final int photosightUserId;
	private String photosightUserName;
	private String photosightUserCardUrl;

	private boolean photosightUserFound;

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
}
